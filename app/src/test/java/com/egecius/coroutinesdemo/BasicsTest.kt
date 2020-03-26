package com.egecius.coroutinesdemo

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

class BasicsTest {

    @Test
    fun `runBlocking blocks the thread until it completes`() {

        runBlocking {
            println("before delay()")
            delay(100)
            println("after delay()")
        }

        println("end of test")
    }
}