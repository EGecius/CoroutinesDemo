package com.egecius.coroutinesdemo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class FlowCompletionTest {

    private val flow: Flow<Int> = flow {
        for (i in 1..3) {
            println("emitting $i")
            emit(i) // emit next value
        }
    }

    @Test
    fun `onCompletion operator is executed once the whole Flow completes`() = runBlocking {
        flow
            .onCompletion { println("Done") }
            .collect { println("collecting $it") }
    }

    @Test
    fun `onCompletion operator also receives a Throwable`() = runBlocking {
        val flow: Flow<Int> = flow {
            emit(1)
            throw RuntimeException()
        }

        flow
            .onCompletion { cause -> if (cause != null) println("Flow completed exceptionally") }
            .catch { cause -> println("Caught exception") }
            .collect { value -> println(value) }
    }
} 