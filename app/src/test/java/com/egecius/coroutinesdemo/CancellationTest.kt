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


    @Test
    fun `passing supervisor job breaks cancellation hierarchy of structured concurrency - passed job will become the parent of the coroutine`() =
        runBlocking {

            var wasSupervisorJobCancelled = false

            val scope = CoroutineScope(Job())
            scope.launch {

                // this SupervisorJob will be become take the spot of being the parent of the coroutine job, thus preventing the scope from ever cancelling it
                launch(SupervisorJob()) {
                    println("performing some work in Coroutine")
                    delay(100)
                }.invokeOnCompletion { throwable: Throwable? ->
                    println("invokeOnCompletion with throwable: $throwable")
                    if (throwable is CancellationException) {
                        wasSupervisorJobCancelled = true
                    }
                }
            }

            // cancel scope while Coroutine performs work
            delay(50)
            scope.cancel()

            assertThat(wasSupervisorJobCancelled).isFalse
            Unit
        }

    @Test
    fun `avoid passing a job to a coroutine keeps the usual cancellation hierarchy, thus allowing the scope to cancel it`() = runBlocking {

        var wasSupervisorJobCancelled = false

        val scope = CoroutineScope(Job())
        scope.launch {

            // not passing a job as param this time
            launch {
                println("performing some work in Coroutine")
                delay(100)
            }.invokeOnCompletion { throwable: Throwable? ->
                println("invokeOnCompletion with throwable: $throwable")
                if (throwable is CancellationException) {
                    println("invokeOnCompletion setting to true")
                    wasSupervisorJobCancelled = true
                }
            }
        }

        // cancel scope while Coroutine performs work
        delay(50)
        scope.cancel()
        delay(100)

        // unlike the above the coroutine will get cancelled
        assertThat(wasSupervisorJobCancelled).isTrue
        Unit
    }
}
