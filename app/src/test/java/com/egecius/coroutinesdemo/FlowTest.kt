package com.egecius.coroutinesdemo

import org.junit.Test

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
}