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

                val time0 = System.currentTimeMillis()

                val deferred1: Deferred<String> = async {
                    delay(500L)
                    "result 1"
                }
                val deferred2: Deferred<String> = async {
                    delay(2000L)
                    "result 2"
                }

                val result1 = deferred1.await() // now this will suspend execution of this block until deferred1 returns
                val diff1 = System.currentTimeMillis() - time0
                Log.v("Eg:AsyncAwaitActivity:32", "setupClickListener() result1: $result1 in $diff1 ms")
                val result2 = deferred2.await() // again, block execution will be returned until deferred2 returns
                val diff2 = System.currentTimeMillis() - time0
                Log.v("Eg:AsyncAwaitActivity:32", "setupClickListener() result1: $result2 in $diff2 ms")
            }
        }
    }
}