package com.egecius.coroutinesdemo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test

class FlowExceptionTest {

    @Test
    fun `you can catch exceptions thrown in collect block`() = runBlocking {
        val flow: Flow<Int> = flow {
            for (i in 1..3) {
                println("Emitting $i")
                emit(i) // emit next value
            }
        }

        try {
            flow.collect {
                println(it)
                check(it <= 1) { "Collected $it" }
            }
        } catch (e: Exception) {
            println(e)
        }
    }
} 