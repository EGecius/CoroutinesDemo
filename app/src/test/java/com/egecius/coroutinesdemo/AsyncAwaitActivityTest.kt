package com.egecius.coroutinesdemo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.egecius.coroutinesdemo.util.MainCoroutineRule
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock

import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.CancellationException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AsyncAwaitActivityTest {

    private var sut: AsyncAwaitActivity? = null

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    // Sets the main coroutines dispatcher to a TestCoroutineScope for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        sut = AsyncAwaitActivity()
    }
    
    @Test
    fun `job allows cancelling a coroutine`() {
        var cancelMessage : String? = null
        val job: Job = CoroutineScope(Main).launch {
            delay(10)
        }
        job.invokeOnCompletion {
            cancelMessage = it?.message
        }

        job.cancel(CancellationException("egis"))

        assertThat(cancelMessage).isEqualTo("egis")
    }

    @Test
    fun `job's isComplete property returns true after it finishes`() = runBlockingTest {
        val job: Job = launch {
            delay(10)
        }

        job.join()
        assertThat(job.isCompleted).isTrue()
    }


    @Test
    fun `job's isCancelled & isComplete properties returns true after it's cancelled`() = runBlockingTest {
        val job: Job = launch {
            delay(10)
        }

        job.cancel()
        assertThat(job.isCancelled).isTrue()
        assertThat(job.isCompleted).isTrue()
    }

}