package com.egecius.coroutinesdemo

import com.egecius.coroutinesdemo.util.log
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@ExperimentalCoroutinesApi
class CoroutineContextAndDispatchersTest {

    @Test
    fun `coroutine inherits context, including dispatcher, from the scope it's launched from`() = runBlocking {

        launch {
            val name = Thread.currentThread().name
            // since runBlocking runs on main thread, main thread will be printed
            print(name)
            assertThat(name).contains("main")
        }
        Unit
    }

    @Test
    fun `coroutine started with its own IO dispatcher does not inherit a dispatcher from parent`() = runBlocking {
        launch(IO) {
            val name = Thread.currentThread().name
            // since runBlocking runs on main thread, main thread will be printed
            print(name)
            assertThat(name).contains("worker")
        }
        Unit
    }

    @Test
    fun `you can run in your own thread`() = runBlockingTest {
        @Suppress("EXPERIMENTAL_API_USAGE")
        // A dedicated thread is a very expensive resource, so would have to be used with special care
        launch(newSingleThreadContext("MyOwnThread")) { // will get its own new thread
            println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
        }
    }

    @Test
    fun `you can run in unconfined Dispatcher`() = runBlockingTest {
        launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
            println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
        }
    }

    @Test
    fun `unconfined coroutines are fully determined by the caller thread`() = runBlocking<Unit> {
        launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
            println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
            delay(50)
            // now  unconfined one resumes in the default executor thread that the delay function is using
            println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
        }
        launch { // context of the parent, main runBlocking coroutine
            println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
            delay(200)
            println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
        }
    }

    @Test
    fun `debug coroutines`() = runBlocking {
        val a = async {
            println("1 child: $coroutineContext")
            6
        }
        val b = async {
            println(coroutineContext)
            println("2 child: $coroutineContext")
            7
        }
        val c = async {
            println(coroutineContext)
            println("3 child: $coroutineContext")
            2
        }
        println("Main: The answer is ${a.await() * b.await() * c.await()} in $coroutineContext")
    }

    @Test
    fun `withContext() changes context but keeps you in the same coroutine`() = runBlocking {
        newSingleThreadContext("Ctx1").use { ctx1 ->
            newSingleThreadContext("Ctx2").use { ctx2 ->

                runBlocking(ctx1) {
                    log("Started in ctx1")
                    withContext(ctx2) {
                        log("Working in ctx2")
                    }
                    log("Back to ctx1")
                }
            }
        }
    }

    @Test
    fun `you can print coroutine job`() = runBlocking{
        println("My job is ${coroutineContext[Job]}")
    }

}