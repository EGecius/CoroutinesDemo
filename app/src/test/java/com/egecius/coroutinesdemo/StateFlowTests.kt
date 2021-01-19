package com.egecius.coroutinesdemo

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Suppress("EXPERIMENTAL_API_USAGE")
class StateFlowTests {

    @Test
    fun `stateFlow emits last value by default`() = runBlockingTest {
        val stateFlow = MutableStateFlow(8)

        stateFlow.take(1).test {
            expectItem() shouldBe 8
            expectComplete()
        }
    }
}
