package com.egecius.coroutinesdemo

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

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
}