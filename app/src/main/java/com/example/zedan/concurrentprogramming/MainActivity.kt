package com.example.zedan.concurrentprogramming

import android.app.Activity
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.ScrollView
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
        val resultReceiver  = MyResultReceiver(Handler())
        MyIntentService.startAction(this, FILE_URL, resultReceiver)
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

    private inner class MyResultReceiver(handler: Handler):
        ResultReceiver(handler){

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            if (resultCode == Activity.RESULT_OK){
                val fileContents = resultData?.getString(FILE_CONTENTS_KEY) ?: "Null"
                log(fileContents)
            }
        }
    }



    companion object{
        private const val FILE_URL = "https://2833069.youcanlearnit.net/lorem_ipsum.txt"
        const val FILE_CONTENTS_KEY = "file_contents_key"
        private const val LOG_TAG = "CodeRunner"
    }
}