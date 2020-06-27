package com.example.zedan.concurrentprogramming

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
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

        Handler().postAtTime( {log("Posting at a certain time.")} , SystemClock.uptimeMillis() + 4000)

        //The value that you pass into the postDelayed function
        //indicates how long you want to wait from the time the code was executed.
        Handler().postDelayed({ log("Operation from runnable 1.") }, 3000)
        Handler().postDelayed({ log("Operation from runnable 2.") }, 2000)
        Handler().postDelayed({ log("Operation from runnable 3.") }, 1000)
        log("Synchronous operation 1.")
        log("Synchronous operation 2.")
        log("Synchronous operation 3.")
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
        private const val LOG_TAG = "MainActivity"
    }
}