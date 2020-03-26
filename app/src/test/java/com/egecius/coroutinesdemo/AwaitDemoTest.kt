package com.egecius.coroutinesdemo

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@ExperimentalCoroutinesApi
class AwaitDemoTest {

    @Test (expected = CancellationException::class)
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