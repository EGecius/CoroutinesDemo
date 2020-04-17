package com.egecius.coroutinesdemo

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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
        myFlow.collect {
            println("result: $it")
        }
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
        myFlow.collect {
            println(it)

        }
        println("Calling collect again...")
        myFlow.collect {
            println(it)
        }
    }

    @Test
    fun `flows get cancelled with the same rules as coroutines`() = runBlockingTest {

        fun foo(): Flow<Int> = flow {
            for (i in 1..3) {
                delay(100)
                println("Emitting $i")
                emit(i)
            }
        }

        withTimeoutOrNull(250) { // Timeout after 250ms
            foo().collect {
                println(it)
            }
        }
        println("Done")
    }

    @Test
    fun `flowOf() is a simpler flow builder`() = runBlockingTest {

        flowOf(1, 2, 3).collect {
            // will print 1, 2, 3
            println(it)
        }
    }

    @Test
    fun `asFlow() is a simple flow builder`() = runBlockingTest {
        // will print 1, 2, 3
        (1..3).asFlow().collect { println(it) }
    }

    @Test
    fun `flows can call intermediate operators`() = runBlockingTest {
        (1..3).asFlow() // a flow of requests
            .map { request -> performRequest(request) }
            .collect { response -> println(response) }
    }

    private suspend fun performRequest(request: Int): String {
        delay(100) // imitate long-running asynchronous work
        return "response $request"
    }

    @Test
    fun `transform() operator allows emitting multiple times & multiple types in a single block`() = runBlockingTest {
        (1..3).asFlow()
            .transform {
                // can emit multiple times and multiple types in a block
                val request: Int = it
                emit(request)
                val result: String = performRequest(request)
                emit(result)
            }
            .collect { value -> println(value) }
    }
}