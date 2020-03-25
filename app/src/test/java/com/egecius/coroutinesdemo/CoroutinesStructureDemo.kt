package com.egecius.coroutinesdemo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.egecius.coroutinesdemo.util.MainCoroutineRule
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.ComparisonFailure
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.CancellationException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CoroutinesStructureDemo {

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
        var cancelMessage: String? = null
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

    @Test
    fun `different contexts are printed for parent & child`() = runBlockingTest {

        // this talk suggested that by default a new coroutine inherits parent context but logs suggest otherwise:
        // https://youtu.be/w0kfnydnFWI?t=290

        launch {

            print("parent context:\n")
            print(coroutineContext)

            launch {

                print(":\nchild context:\n")
                print(coroutineContext)


            }
        }
    }

    @Test
    fun `cancelling parent scope cancels all children`() = runBlockingTest {

        var hasRunChild1 = false
        var hasRunChild2 = false
        launch {

            launch {
                print("\nstarting child1:")
                hasRunChild1 = true
                print("\ncompleted child1")
            }

            launch {
                print("\nstarting child2:")
                // this one has longer delay, so should not be run
                delay(300)

                hasRunChild2 = true
                print("\ncompleted child2")
            }

            // cancelling parent scope will cancel children that have not finished
            delay(100)
            print("\ncancelling parent")
            cancel()
        }

        assertThat(hasRunChild1).isTrue()
        assertThat(hasRunChild2).isFalse()
    }

    @Test
    fun `values set after delay() do not persist`() = runBlockingTest {
        var hasRunChild1: String? = null
        launch {

            launch {
                print("\nstarting child1:")
                delay(1)
                hasRunChild1 = "set from child coroutine"
                print("\ncompleted child1")
            }
        }

        // for some weird reason if delay is called prior to a value being set, it won't be set
        assertThat(hasRunChild1).isNull()
    }

}