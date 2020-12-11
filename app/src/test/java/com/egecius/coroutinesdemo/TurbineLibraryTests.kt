package com.egecius.coroutinesdemo

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class TurbineLibraryTests {

    @Test
    fun `asserts that certain items were emitted`() = runBlocking {

        flowOf(7, 9).test {
            assertThat(expectItem()).isEqualTo(7)
            assertThat(expectItem()).isEqualTo(9)
            expectComplete()
        }

    }
}
