package com.egecius.coroutinesdemo

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Ignore
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class ControllingVirtualTime {

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
    fun `'runBlocking' does not auto-advance virtual time`() = runBlocking {
        val foo = returnAfter1sDelay()
        foo shouldBe "delayed"
    }

    @Test
    @Ignore("for some reason does not work with Flow")
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
