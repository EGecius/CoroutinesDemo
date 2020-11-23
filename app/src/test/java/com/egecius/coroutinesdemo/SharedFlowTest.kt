package com.egecius.coroutinesdemo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.egecius.coroutinesdemo.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SharedFlowTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test @Ignore("fails with .IllegalStateException: This job has not completed yet")
    fun `shared flow can be consumed by multiple sources`() = runBlockingTest {

        val mutableSharedFlow = MutableSharedFlow<String>()

        val result0 = mutableSharedFlow.firstOrNull()

//        // initially it's empty
        assertThat(result0).isEmpty()

        mutableSharedFlow.emit("event-1")
        val result1 = mutableSharedFlow.toList()
        assertThat(result1).isEqualTo("event-1")
    }
}
