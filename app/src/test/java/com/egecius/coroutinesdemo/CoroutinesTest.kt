package com.egecius.coroutinesdemo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class CoroutinesTest {

    /** Produces infinite stream */
    private fun CoroutineScope.produceNumbers() = produce {
        var x = 1
        while (true) send(x++) // infinite stream of integers starting from 1
    }

    /** squares every received value*/
    private fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
        for (x in numbers) send(x * x)
    }

    @Test
    fun `a pipeline is a pattern where one coroutine is producing, possibly infinite, stream of values`() = runBlocking {
        val numbers = produceNumbers() // produces integers from 1 and on
        val squares = square(numbers) // squares integers
        repeat(5) {
            println(squares.receive()) // print first five
        }
        println("Done!") // we are done
        coroutineContext.cancelChildren() // cancel children coroutines
    }
}