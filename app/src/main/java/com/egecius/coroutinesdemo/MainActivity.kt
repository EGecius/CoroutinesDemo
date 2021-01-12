package com.egecius.coroutinesdemo

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val model: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setClickListener()

        demoLaunchWhenStarted()
    }

    private fun demoLaunchWhenStarted() {
        // Flow will pause emitting on onStop() but will continue emitting on onStart()
        lifecycleScope.launchWhenStarted {
            neverEndingEmittingFlow().collect {
                Log.v("Eg:MainActivity:29", "onCreate() it: $it")
            }
        }

        Log.v("Eg:MainActivity:35", "onCreate() emitIn5s")

        // Coroutine will keep running after onStop() but will emit only on onStart()
        lifecycleScope.launchWhenStarted {
            val result = emitIn5s()
            Log.v("Eg:MainActivity:38", "onCreate() result: $result")
        }
    }

    private suspend fun emitIn5s(): String {
        delay(5_000)
        return "emission after 5s"
    }

    private fun setClickListener() {
        button.setOnClickListener {
            fetchFakeApiRequestAndUpdateUi()
        }
    }

    private fun fetchFakeApiRequestAndUpdateUi() {
        CoroutineScope(IO).launch {
            // starting in IO thread
            val result1 = getResultFromApi(RESULT_1)
            println("debug: $result1")
            // now will switch to Main thread to update the UI:
            setTextOnMainThread(result1)
            // now will get another result which still will be fetched on IO thread:
            val result2 = getResultFromApi(RESULT_2)
            println("debug: $result2")
            // now will switch to Main thread to update the UI:
            setTextOnMainThread(result2)
        }
    }

    private suspend fun getResultFromApi(result: String): String {
        logThread("getResult1FromApi")
        delay(1000)
        return result
    }

    private fun logThread(methodName: String) {
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext(Main) {
            val newText = text_view.text.toString() + "\n" + input
            text_view.text = newText
        }
    }

    companion object {
        private const val RESULT_1 = "Result 1"
        private const val RESULT_2 = "Result 2"
    }
}

fun neverEndingEmittingFlow(): Flow<Unit> {
    return flow {
        while (true) {
            delay(1_000)
            emit(Unit)
        }
    }
}
