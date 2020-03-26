package com.egecius.coroutinesdemo

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class CoroutineContextAndDispatchersTest {
    
    @Test
    fun `coroutine inherits context, including dispatcher, from the scope it's launched from`() = runBlocking {

        launch {
            val name = Thread.currentThread().name
            // since runBlocking runs on main thread, main thread will be printed
            print(name)
        }
        Unit
    }
}