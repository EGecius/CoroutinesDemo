package com.egecius.coroutinesdemo

import com.egecius.coroutinesdemo.util.neverEndingEmittingFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class FlowNeverEndingTest {
    
    @Test
    fun `a never ending emitting flow can be terminated by using 'take'`() = runBlockingTest {

        neverEndingEmittingFlow().take(1).collect {  }
    }
}
