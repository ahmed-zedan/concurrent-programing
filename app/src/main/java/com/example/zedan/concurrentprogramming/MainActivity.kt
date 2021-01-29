package com.example.zedan.concurrentprogramming

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.ScrollView
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.zedan.concurrentprogramming.MyWorker.Companion.DATA_KEY
import com.example.zedan.concurrentprogramming.MyWorker.Companion.MESSAGE_KEY
import com.example.zedan.concurrentprogramming.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding for view object references
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            runButton.setOnClickListener { runCode() }
            clearButton.setOnClickListener { clearOutput() }
        }
    }

    /**
     * Run some code
     */
    private fun runCode(){
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest= OneTimeWorkRequestBuilder<MyWorker>()
            .setConstraints(constraint)
            .build()

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueue(workRequest)
        workManager.getWorkInfoByIdLiveData(workRequest.id)
            .observe(this, Observer {
                when (it.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        val result = it.outputData.getString(DATA_KEY) ?: "Null"
                        log(result)
                    }
                    WorkInfo.State.RUNNING -> {
                        val progress = it.progress.getString(MESSAGE_KEY)
                        progress?.let{m -> log(m) }
                    }
                }
            })
    }

    /**
     * clear log display
     */
    private fun clearOutput(){
        binding.logDisplay.text = ""
    }

    /**
     * Log output to logcat and the screen
     */
    @Suppress("SameParameterValue")
    private fun log(message: String) {
        Log.i(LOG_TAG, message)
        binding.logDisplay.append(message + "\n")
        scrollTextToEnd()
    }

    /**
     * Scroll to the end. Wrapped in post() function so it's the last thing to happen
     */
    private fun scrollTextToEnd(){
        Handler().post { binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }



    companion object{
        private const val FILE_URL = "https://2833069.youcanlearnit.net/lorem_ipsum.txt"
        private const val LOG_TAG = "CodeRunner"
    }
}