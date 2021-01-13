@file:Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")

package com.egecius.coroutinesdemo

import com.egecius.coroutinesdemo.util.failingCoroutine
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

    @Test
    fun `exception thrown in async block is not caught in try catch -- it's encapsulated inside Deferred object instead`() {

        var wasCaughtInTryCatchBlock = false

        CoroutineScope(Job()).launch {
            // this try/catch block won't catch it since Exception thrown in async block is encapsulated inside Deferred object
            try {
                async {
                    throw EgisException()
                }
            } catch (e: Exception) {
                wasCaughtInTryCatchBlock = true
            }
        }

        assertThat(wasCaughtInTryCatchBlock).isFalse
    }

    @Test
    fun `exception thrown in async block can be caught when called await() on Deferred`() {
        var wasCaughtInTryCatchBlock = false
        val topLevelScope = CoroutineScope(SupervisorJob())

        val deferred = topLevelScope.async {
            throw EgisException()
        }

        topLevelScope.launch {
            try {
                deferred.await()
            } catch (e: Exception) {
                println("caught: $e")
                wasCaughtInTryCatchBlock = true
            }
        }

        Thread.sleep(200)

        assertThat(wasCaughtInTryCatchBlock).isTrue
    }

    @Test
    fun `when async is not a top level coroutine exception is thrown without waiting for await() call`() {

        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
            println("Handle $exception in CoroutineExceptionHandler")
        }

        val topLevelScope = CoroutineScope(SupervisorJob() + coroutineExceptionHandler)
        topLevelScope.launch {
            // async not at the top level - exception will be thrown without waiting for await() call
            async {
                throw RuntimeException("RuntimeException in async coroutine")
            }
        }
        Thread.sleep(100)
    }

    @Test
    fun `using coroutineScope() does not re-throw exceptions up the hierarchy tree`() {

        var wasCaughtInTryCatchBlock = false

        CoroutineScope(Job()).launch {

            coroutineScope {
                try {
                    failingCoroutine()
                } catch (e: Exception) {
                    wasCaughtInTryCatchBlock = true
                    println("exception caught using coroutineScope: $e")
                }
            }
        }

        Thread.sleep(100)

        assertThat(wasCaughtInTryCatchBlock).isTrue
    }
}
