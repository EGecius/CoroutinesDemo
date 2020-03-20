package com.egecius.coroutinesdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setClickListener()
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
