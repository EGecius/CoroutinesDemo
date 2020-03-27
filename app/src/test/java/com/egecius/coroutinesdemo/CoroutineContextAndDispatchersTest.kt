package com.egecius.coroutinesdemo

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.NonCancellable.join
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
    fun `unconfined coroutines are fully determined by the caller thread`() = runBlocking<Unit>{
        launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
            println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
            delay(50)
            println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
        }
        launch { // context of the parent, main runBlocking coroutine
            println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
            delay(200)
            println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
        }
    }
}