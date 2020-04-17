package com.egecius.coroutinesdemo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class FlowTest {

    @Test
    fun `show how to create sequence flow`() {

        fun foo(): Sequence<Int> = sequence { // sequence builder
            for (i in 1..3) {
                Thread.sleep(100) // pretend we are computing it
                yield(i) // yield next value
            }
        }

        foo().forEach { value -> println(value) }

    }

    @Test
    fun `show how create a flow`() = runBlockingTest {

        val myFlow = flow { // flow builder
            for (i in 1..3) {
                emit(i) // emit next value
            }
        }

        // Launch a concurrent coroutine to check if the main thread is blocked
        launch {
            for (k in 1..3) {
                println("I'm not blocked $k")
                delay(100)
            }
        }
        // Collect the flow
        myFlow.collect(object : FlowCollector<Int> {
            override suspend fun emit(value: Int) {
                println("result: $value")
            }
        })
    }

    @Test
    fun `flows are cold - execution does not start until collect() is called`() = runBlockingTest {

        val myFlow = flow {
            println("Flow started")
            for (i in 1..3) {
                delay(100)
                emit(i)
            }
        }

        println("Calling collect...")
        myFlow.collect(object : FlowCollector<Int> {
            override suspend fun emit(value: Int) {
                println(value)
            }
        })
        println("Calling collect again...")
        myFlow.collect(object : FlowCollector<Int> {
            override suspend fun emit(value: Int) {
                println(value)
            }
        })
    }
}