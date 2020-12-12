package com.egecius.coroutinesdemo

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import kotlin.time.ExperimentalTime

@ObsoleteCoroutinesApi
@ExperimentalTime
@ExperimentalCoroutinesApi
class ControllingVirtualTime {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setup() {
    	Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `'runBlockingTest' auto-advances virtual time`() = runBlockingTest {
    	val foo = delayingFoo()
        foo shouldBe "delayed"
    }

    private suspend fun delayingFoo(): String {
        delay(1000)
        return "delayed"
    }

    @Test @Ignore("does not work yet")
    fun `shows how to control virtual time`() = runBlockingTest {
        val myFlow = flow {
            delay(1_000)
            emit(13)
        }

        myFlow.test {
            advanceTimeBy(2_000)
            expectItem() shouldBe 13
            expectComplete()
        }
    }
}
