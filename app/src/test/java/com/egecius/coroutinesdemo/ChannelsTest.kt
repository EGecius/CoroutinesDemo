package com.egecius.coroutinesdemo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class ChannelsTest {

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

    @Test
    fun `produce a stream of prime number using pipes`() = runBlocking {

        var currentChannel: ReceiveChannel<Int> = numbersFrom(2)
        repeat(10) {
            val prime: Int = currentChannel.receive()
            println(prime)
            currentChannel = filter(currentChannel, prime)
        }
        coroutineContext.cancelChildren() // cancel all children to let main finish
    }

    private fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
        var x = start
        while (true) send(x++) // infinite stream of integers from start
    }

    private fun CoroutineScope.filter(numbersChannel: ReceiveChannel<Int>, prime: Int) = produce<Int> {
        for (x in numbersChannel) if (x % prime != 0) send(x)
    }
}