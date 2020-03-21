package com.egecius.coroutinesdemo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_job.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class JobActivity : AppCompatActivity() {

    private lateinit var job: CompletableJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job)
        initJob()

        job_button.setOnClickListener {
            startJobOrCancel()
        }
    }

    private fun resetJob() {
        if (job.isActive || job.isCompleted) {
            job.cancel(CancellationException("Resetting job"))
        }
        initJob()
    }

    private fun initJob() {
        job = Job()
        job.invokeOnCompletion {
            it?.message.let { msg ->
                val msgToPrint = if (msg.isNullOrBlank()) "Unknown cancellation error." else msg
                Log.e(TAG, "$job was cancelled. Reason: $msgToPrint")
                showToast(msgToPrint)
            }
        }
        resetViews()
    }

    private fun resetViews() {
        job_progress_bar.max = PROGRESS_MAX
        job_progress_bar.progress = PROGRESS_START
        job_button.text = "Start Job #1"
        updateJobCompleteTextView("")
    }


    private fun startJobOrCancel() {
        if (job_progress_bar.progress > 0) {
            Log.d(TAG, "$job is already active. Cancelling...")
            resetJob()
        } else {
            job_button.text = "Cancel Job #1"
            CoroutineScope(IO + job).launch {
                Log.d(TAG, "coroutine $this is activated with job $job.")

                for (i in PROGRESS_START..PROGRESS_MAX) {
                    delay((JOB_TIME / PROGRESS_MAX).toLong())
                    job_progress_bar.progress = i
                }
                updateJobCompleteTextView("Job is complete!")
            }
        }
    }

    private fun updateJobCompleteTextView(text: String) {
        GlobalScope.launch(Main) {
            job_complete_text.text = text
        }
    }

    private fun showToast(text: String) {
        GlobalScope.launch(Main) {
            Toast.makeText(this@JobActivity, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {

        private const val TAG: String = "AppDebug"

        private const val PROGRESS_MAX = 100
        private const val PROGRESS_START = 0
        private const val JOB_TIME = 4000 // ms
    }
}