package com.egecius.coroutinesdemo

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

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
}