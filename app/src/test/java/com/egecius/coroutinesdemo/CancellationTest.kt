package com.egecius.coroutinesdemo

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@ExperimentalCoroutinesApi
class CancellationTest {

    @Test
    fun `after cancelling built-in coroutines throw a CancellationException`() = runBlocking {

        val job = launch {
            try {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } catch (e: Exception) {
                println("job: I caught: $e")
            }
        }
        delay(1300L) // delay a bit
        println("main: I'm tired of waiting! Cancelling...")
        job.cancelAndJoin() // cancels the job and waits for its completion
        println("main: Now I can quit.")
    }

    @Test(expected = TimeoutCancellationException::class)
    fun `withTimeout() causes a TimeoutCancellationException`() = runBlocking {
        withTimeout(1300L) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
    }

    @Test
    fun `withTimeout() returns a value`() = runBlockingTest {
        val result: String = withTimeout(1000) {
            repeat(3) { i ->
                println("I'm sleeping $i ...")
                delay(10L)
            }
            "egis"
        }

        assertThat(result).isEqualTo("egis")
    }

    @Test
    fun `withTimeoutOrNull() does not throw a TimeoutCancellationException`() = runBlockingTest {
        val result = withTimeoutOrNull(100) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(40)
            }
        }

        assertThat(result).isNull()
    }
}