package com.egecius.coroutinesdemo

import android.content.ClipData
import app.cash.turbine.Event
import app.cash.turbine.test
import com.egecius.coroutinesdemo.util.neverEndingEmptyFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
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

    @Test(expected = TimeoutCancellationException::class)
    fun `checks if a flow completed`() = runBlocking {
        neverEndingEmptyFlow().test {
            expectComplete()
        }
    }

    @Test(expected = AssertionError::class)
    fun `throws assertion error if complete event is found`() = runBlockingTest {
        emptyFlow<Int>().test {
            expectNoEvents()
        }
    }

    @Test
    fun `test passes if no events are found, including no completion event`() = runBlockingTest {
        neverEndingEmptyFlow().test {
            expectNoEvents()
        }
    }

    @Test
    fun `allows to assert events with more detail`() = runBlockingTest {
        flowOf(67).test {
            assertThat(expectEvent()).isEqualTo(Event.Item(67))
            assertThat(expectEvent()).isEqualTo(Event.Complete)
        }
    }

    @Test (expected = AssertionError::class)
    fun `fails if certain events are not complete`() = runBlockingTest {
    	flowOf(1).test {
    	    assertThat(expectItem()).isEqualTo(1)
        }
    }
}
