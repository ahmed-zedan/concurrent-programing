package com.example.zedan.concurrentprogramming

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.ScrollView
import com.example.zedan.concurrentprogramming.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val handler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            val bundle = msg.data
            val message = bundle.getString(MESSAGE_KEY)
            log(message ?: "message was null.")
        }
    }

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
        // Use bundle and message to pass data UIThread.
        thread(start = true) {
            val bundle = Bundle()
            for (i in 0..10){
                bundle.putString(MESSAGE_KEY, "Lopping $i")
                Message().also {
                    it.data = bundle
                    handler.sendMessage(it)
                }
                Thread.sleep(1000)
            }
            bundle.putString(MESSAGE_KEY, "All done.")
            Message().also {
                it.data = bundle
                handler.sendMessage(it)
            }
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
        private const val  MESSAGE_KEY = "message_key"
        private const val LOG_TAG = "CodeRunner"
    }
}