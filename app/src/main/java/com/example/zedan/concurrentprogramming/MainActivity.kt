package com.example.zedan.concurrentprogramming

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.ScrollView
import com.example.zedan.concurrentprogramming.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.net.URL
import java.nio.charset.Charset

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
            log(result ?: "Null")
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

    private suspend fun fetchSomething(): String?{
        log("Requesting file started")
        return withContext(Dispatchers.IO) {
            val url = URL(fileUlr)
            return@withContext url.readText(Charset.defaultCharset())
        }
    }

    companion object{
        private const val fileUlr = "https://2833069.youcanlearnit.net/lorem_ipsum.txt"
        private const val LOG_TAG = "CodeRunner"
    }
}