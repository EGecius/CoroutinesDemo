package com.egecius.coroutinesdemo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.egecius.coroutinesdemo.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FlowToLiveDataTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Ignore // TODO: 09/10/2020 fix failing test
    @Test
    fun `easy to append initial value to flow and covert to live data`() = runBlockingTest {

        val liveData = flowOf(1, 2, 3)
            .onStart { emit(0) }
            .asLiveData()

        // TODO: 18/04/2020 learn how to test LiveData
        val result: Int? = liveData.getOrAwaitValue()

        assertThat(result).isEqualTo(5)
    }
}
