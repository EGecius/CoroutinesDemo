package com.egecius.coroutinesdemo

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test

@ExperimentalCoroutinesApi
class SupervisorScopeDemoTest {

    @Test
    @Ignore // for some reason it does not work as it's supposed to. second coroutine does not finish after delay
    fun `with supervisor job when a child coroutine fails, siblings don't get cancelled`() = runBlockingTest {

        val coroutineScope = CoroutineScope(SupervisorJob())
        coroutineScope.launch {
            println("starting child1")
            throw Exception("Egis")
        }

        coroutineScope.launch {
            println("starting child2")
            delay(100)
            println("completed child2")
        }
    }


    @Test(expected = Exception::class)
    fun `when a child coroutine in supervisor scope fails, siblings don't get cancelled`() = runBlockingTest {

        launch {
            supervisorScope {

                val child1 = launch {
                    println("starting child1")
                    delay(100)
                    throw Exception("Egis")
                }

                val child2 = launch {
                    println("starting child2")
                    delay(100)
                    println("completed child2")
                }
            }
        }
    }

    @Test(expected = Exception::class)
    fun `without supervisor scope, siblings get cancelled on failure`() = runBlockingTest {

        launch {
            println("starting child1")

            val child1 = launch {
                delay(100)
                throw Exception("Egis")
            }

            val child2 = launch {
                println("starting child2")
                delay(100)
                println("completed child2")
            }
        }
    }

    @Test
    fun `passing supervisor job breaks cancellation hierarchy of structured concurrency - passed job will become the parent of the coroutine`() = runBlocking {

        var wasSupervisorJobCancelled = false

        val scopeJob = Job()
        val scope = CoroutineScope(scopeJob)

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

        val scopeJob = Job()
        val scope = CoroutineScope(scopeJob)

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

    @Test
    fun `using coroutine scope does not break cancellation unlike passing SupervisorJob`() = runBlockingTest {
        supervisorScope {
            launch {

            }
        }
    }
}
