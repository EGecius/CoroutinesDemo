package com.egecius.coroutinesdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_job.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class JobActivity : AppCompatActivity() {

    private lateinit var job: CompletableJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job)
        initJob()

        job_button.setOnClickListener {
            startOrResetJob()
        }
    }

    private fun startOrResetJob() {
        if (job_progress_bar.progress > 0) {
            resetJob()
        } else {
            startJob()
        }
    }

    private fun resetJob() {
        job.cancel()
        initJob()
        resetViews()
    }

    private fun resetViews() {
        job_progress_bar.progress = PROGRESS_START
        job_progress_bar.max = PROGRESS_MAX
        job_button.text = "Start"
    }

    private fun startJob() {
        CoroutineScope(IO + job).launch {
            for (i in PROGRESS_START..PROGRESS_MAX) {
                delay(DELAY)
                job_progress_bar.progress = i
            }
        }
    }

    private fun initJob() {
        job = Job()
        job.invokeOnCompletion {
            val msg = it?.message ?: "unknown cause"
            showToast(msg)
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {

        private const val TAG: String = "AppDebug"

        private const val PROGRESS_MAX = 100
        private const val PROGRESS_START = 0
        private const val JOB_TIME = 2000 // ms
        private const val DELAY = (JOB_TIME / PROGRESS_MAX).toLong()
    }
}