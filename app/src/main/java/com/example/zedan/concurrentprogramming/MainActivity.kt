package com.example.zedan.concurrentprogramming

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.ScrollView
import com.example.zedan.concurrentprogramming.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var myService: MyService

    private val connection = object : ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(LOG_TAG, "onServiceDisconnected: call.")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(LOG_TAG, "onServiceConnected: call.")
            val binder = service as MyService.MyBindService
            myService = binder.getService()
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

    override fun onStart() {
        super.onStart()
        Intent(this, MyService::class.java).also {
            bindService(it, connection, Context.BIND_AUTO_CREATE)
            startService(it)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }
    /**
     *
     * Run some code
     */
    private fun runCode(){
        myService.startMusic()
    }

    /**
     * clear log display
     */
    private fun clearOutput(){
        myService.stopMusic()
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