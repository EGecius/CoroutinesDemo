package com.egecius.coroutinesdemo

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
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
}