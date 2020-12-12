package com.egecius.coroutinesdemo

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class FlowExceptions {

    @Test
    fun `'retry' allows retry if a certain condition is met`() = runBlockingTest {

        var count = 0

        val flow = flow<Int> {
            if (count < 2) {
                throw EgisException("demo 1")
            } else {
                throw RuntimeException("from in RuntimeException")
            }
        }.retry { throwable ->
            count++
            // will retry if this condition is met
            throwable is EgisException
        }

        flow.test {
            assertThat(expectError().message).isEqualTo("from in RuntimeException")
        }

        assertThat(count).isEqualTo(3)
    }
}
