package com.egecius.coroutinesdemo

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
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

    @Test
    fun `having 100k coroutines complete takes less than 1s`() = runBlockingTest {
        val deferredList: List<Deferred<Int>> = (1..100_000).map { n ->
            GlobalScope.async {
                n
            }
        }

        val sum = deferredList.map { it.await().toLong() }.sum()

        println(sum)
    }
}