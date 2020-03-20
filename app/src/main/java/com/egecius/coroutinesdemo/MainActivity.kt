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
            CoroutineScope(IO).launch {
                fetchFakeApiRequestAndUpdateUi()
            }
        }
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext(Main) {
            val newText = text_view.text.toString() + "\n" + input
            text_view.text = newText
        }
    }

    private suspend fun fetchFakeApiRequestAndUpdateUi() {
        val result1 = getResult1FromApi()
        println("debug: $result1")
        setTextOnMainThread(result1)
    }

    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    private fun logThread(methodName: String) {
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }

    companion object {
        private const val RESULT_1 = "Result 1"
    }
}
