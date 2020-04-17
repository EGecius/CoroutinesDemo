package com.egecius.coroutinesdemo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
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
} 