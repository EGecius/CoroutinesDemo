package com.egecius.coroutinesdemo

import com.egecius.coroutinesdemo.util.neverEndingEmittingFlow
import com.egecius.coroutinesdemo.util.neverEndingEmptyFlow
import junit.framework.Assert.fail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Ignore
import org.junit.Test

@ExperimentalCoroutinesApi
class FlowNeverEndingTest {

    @Test @Ignore("will never complete")
    fun `never ending empty flow will block the test`() = runBlockingTest {
        // this will never complete & block the test
        neverEndingEmptyFlow().collect {  }
    }
    
    @Test
    fun `a never ending emitting flow can be terminated by using 'take'`() = runBlockingTest {

        neverEndingEmittingFlow().take(1).collect {  }
    }

    @Test
    fun `cancelling never-ending flow allows test to finish`() = runBlockingTest {
        val job = launch {
            neverEndingEmptyFlow().collect {  }
        }
        job.cancel()
    }
}
