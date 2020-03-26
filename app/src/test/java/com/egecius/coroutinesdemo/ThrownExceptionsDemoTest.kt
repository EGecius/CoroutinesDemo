package com.egecius.coroutinesdemo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@ExperimentalCoroutinesApi
class ThrownExceptionsDemoTest {
    
    @Test
    fun `within launch() exception gets thrown immediately`() = runBlockingTest{

        var isExceptionCaught = false

        launch {
            try {
                throw Exception("egis")
            } catch (exception: Exception) {
                print("exception caught in my try/catch block:: $exception")
                isExceptionCaught = true
            }
        }

        assertThat(isExceptionCaught).isTrue()
    }

}