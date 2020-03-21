package com.egecius.coroutinesdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_job.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class JobActivity : AppCompatActivity() {

    lateinit var job : Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job)
        initProgressBar()
        setupListener()
        initJob()
    }

    private fun initProgressBar() {
        progressBar.progress = START
        progressBar.max = MAX
    }

    private fun setupListener() {
        button.setOnClickListener {
            if (progressBar.progress > 0) {
                job.cancel()
                initJob()
                progressBar.progress = START
                button.text = getString(R.string.start)
            } else {
                startProgress()
                button.text = getString(R.string.cancel)
            }
        }
    }

    private fun startProgress() {
        CoroutineScope(IO + job).launch {
            for (i in START..MAX) {
                delay(DELAY)
                progressBar.progress = i
            }
        }
    }

    private fun initJob() {
        if (!::job.isInitialized) {
            job = Job()
            job.invokeOnCompletion {
                showToast("complete")
            }
        }
    }

    private fun showToast(message: String) {
        GlobalScope.launch(Main) {
            Toast.makeText(this@JobActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val START = 0
        const val MAX = 100
    	private const val TIME_IN_MS = 4000
        const val DELAY : Long = (TIME_IN_MS / MAX).toLong()

    }
}