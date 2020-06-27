package com.example.zedan.concurrentprogramming

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.widget.ScrollView
import com.example.zedan.concurrentprogramming.databinding.ActivityMainBinding
import kotlin.concurrent.thread

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

        /**
         * Now i can't call [log] method
         * because  I'm running in a background thread.
         * When you're in a background thread, you do not have direct access to the UI
         */
        thread(start = true) {
            for (i in 0..10){
                Log.i(LOG_TAG, "Lopping $i")
                Thread.sleep(1000)
            }
            Log.i(LOG_TAG, "All done.")
        }

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
        private const val LOG_TAG = "CodeRunner"
    }
}