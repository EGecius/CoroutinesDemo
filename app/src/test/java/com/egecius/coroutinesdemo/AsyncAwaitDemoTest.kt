package com.egecius.coroutinesdemo

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@ExperimentalCoroutinesApi
class AsyncAwaitDemoTest {

    @Test
    fun `async block gets executed before await() is called`() = runBlockingTest {

        var isAsyncExecuted = false
        val deferred = async {
            println("executing async")
            isAsyncExecuted = true
        }

        println("avoiding calling await")
//        deferred.await()

        assertThat(isAsyncExecuted).isTrue()
    }

    @Test(expected = CancellationException::class)
    fun `calling await() after job is cancelled fails with exception because there is nothing to return`() = runBlockingTest {

        val deferred: Deferred<String> = async {
            delay(10)
            "result"
        }

        deferred.cancel()
        deferred.await()
    }

    @Test
    fun `calling await() before cancellation returns a result`() = runBlockingTest {

        val deferred: Deferred<String> = async {
            delay(10)
            "egis result"
        }

        val result = deferred.await()
        assertThat(result).isEqualTo("egis result")
    }

    @Test
    fun `after await() returns, job-deferred is complete`() = runBlockingTest {

        val deferred: Deferred<String> = async {
            delay(10)
            "egis result"
        }

        @Suppress("UNUSED_VARIABLE") val result = deferred.await()
        assertThat(deferred.isCompleted).isTrue()
    }
}
