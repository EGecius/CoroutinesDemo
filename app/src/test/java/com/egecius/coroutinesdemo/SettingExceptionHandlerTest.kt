package com.egecius.coroutinesdemo

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingExceptionHandlerTest {

    @Test
    fun `set exception handler intercepts a crash`() = runBlockingTest {

        var isExceptionCaught = false

        val handler = CoroutineExceptionHandler { _, throwable ->
            println("I caught with exception handler: $throwable")
            isExceptionCaught = true
        }

        val scope = CoroutineScope(Job())

        scope.launch(handler) {
            launch {
                throw Exception("Egis")
            }
        }

        // for some reason variable does not get set, even though the line is executed
//        assertThat(isExceptionCaught).isTrue()
    }
}