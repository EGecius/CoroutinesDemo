@file:Suppress("UsePropertyAccessSyntax")

package com.egecius.coroutinesdemo

import androidx.lifecycle.viewModelScope
import com.egecius.coroutinesdemo.util.MainCoroutineRule
import kotlinx.coroutines.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException
import java.util.concurrent.CancellationException

class CoroutineExceptionsTest {

    /** Sets the main coroutines dispatcher to a TestCoroutineScope for unit testing */
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    @Test
    fun `invokeOnCompletion receives exception thrown`() {
        var resultThrowable: Throwable? = null
        CoroutineScope(Dispatchers.Main).launch {
            throw UnknownHostException("egis")
        }.invokeOnCompletion {
            resultThrowable = it
        }

        assertThat(resultThrowable?.message).isEqualTo("egis")
        assertThat(resultThrowable is UnknownHostException).isTrue()
    }

    @Test
    fun `job allows cancelling a coroutine`() {
        var cancelMessage: String? = null
        val job: Job = CoroutineScope(Dispatchers.Main).launch {
            delay(10)
        }
        job.invokeOnCompletion {
            cancelMessage = it?.message
        }

        job.cancel(CancellationException("egis"))

        assertThat(cancelMessage).isEqualTo("egis")
    }

    @Test
    fun `viewModelScope job intercepts exception`() {
        var resultThrowable: Throwable? = null

        MyViewModel().viewModelScope.launch{
            throw Exception("egis")
        }.invokeOnCompletion {
            resultThrowable = it
        }

        assertThat(resultThrowable?.message).isEqualTo("egis")
    }
}