package com.egecius.coroutinesdemo

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class FlowExceptions {

    private var count = 0

    private val failingFlow: Flow<Int> = flow {
        if (count < 2) {
            throw EgisException("demo 1")
        } else {
            throw RuntimeException("from in RuntimeException")
        }
    }

    @Before
    fun setup() {
        count = 0
    }

    @Test
    fun `'retry' allows retry if a certain condition is met`() = runBlockingTest {
        failingFlow.retry { throwable ->
            count++
            // will retry if this condition is met
            throwable is EgisException
        }.test {
            assertThat(expectError().message).isEqualTo("from in RuntimeException")
        }

        assertThat(count).isEqualTo(3)
    }
}
