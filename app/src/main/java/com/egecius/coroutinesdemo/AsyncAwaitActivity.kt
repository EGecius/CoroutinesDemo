package com.egecius.coroutinesdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_async_await.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

@SuppressLint("LongLogTag")
class AsyncAwaitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async_await)
        setupClickListener()
    }

    private fun setupClickListener() {
        button_async_await.setOnClickListener {
            CoroutineScope(IO).launch {

                val deferred1: Deferred<String> = async {
                    delay(1000L)
                    "result 1"
                }
                val deferred2: Deferred<String> = async {
                    delay(2000L)
                    "result 2"
                }

                val result1 = deferred1.await()
                val result2 = deferred2.await()
                Log.v("Eg:AsyncAwaitActivity:32", "setupClickListener() result1: $result1")
                Log.v("Eg:AsyncAwaitActivity:36", "setupClickListener() result2: $result2")
            }
        }
    }
}