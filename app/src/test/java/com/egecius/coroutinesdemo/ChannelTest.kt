package com.egecius.coroutinesdemo

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class ChannelTest {

    @Test
    fun `channels acts like a blockingQueue where 'put' is 'send' & 'take' is 'receive'`() = runBlocking {
        val channel = Channel<Int>()
        launch {
            // this might be heavy CPU-consuming computation or async logic, we'll just send five squares
            for (x in 1..5) channel.send(x * x)
        }
        // here we print five received integers:
        repeat(5) { println(channel.receive()) }
        println("Done!")
    }
}