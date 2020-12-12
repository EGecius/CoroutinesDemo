package com.egecius.coroutinesdemo

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Ignore
import org.junit.Test
import kotlin.time.ExperimentalTime

@ObsoleteCoroutinesApi
@ExperimentalTime
@ExperimentalCoroutinesApi
class ControllingVirtualTime {

//    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
//
//    @Before
//    fun setup() {
//    	Dispatchers.setMain(mainThreadSurrogate)
//    }
//
//    @After
//    fun cleanup() {
//        Dispatchers.resetMain()
//        mainThreadSurrogate.close()
//    }

    @Test
    fun `'runBlockingTest' auto-advances virtual time`() = runBlockingTest {
        val foo = returnAfter1sDelay()
        foo shouldBe "delayed"
    }

    private suspend fun returnAfter1sDelay(): String {
        delay(1000)
        return "delayed"
    }

    @Test(timeout = 100)
    @Ignore("will time out")
    fun `'runBlocking' does not auto-advances virtual time`() = runBlocking {
        val foo = returnAfter1sDelay()
        foo shouldBe "delayed"
    }

    @Test
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
