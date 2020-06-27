package com.example.zedan.concurrentprogramming

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.ScrollView
import com.example.zedan.concurrentprogramming.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        CoroutineScope(Dispatchers.Main).launch {
            val result = fetchSomething()
            log(result)
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

    private suspend fun fetchSomething(): String{
        delay(2000)
        return "Something from API"
    }

    companion object{
        private const val  MESSAGE_KEY = "message_key"
        private const val LOG_TAG = "CodeRunner"
    }
}