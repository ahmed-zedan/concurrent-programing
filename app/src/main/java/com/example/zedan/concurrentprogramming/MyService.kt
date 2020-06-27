package com.example.zedan.concurrentprogramming

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyService : Service() {

    private val binder = MyBindService()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun doSomething(){
        Log.i(TAG, "doSomething: ")
    }

    inner class MyBindService : Binder(){
        fun getService() = this@MyService
    }

    companion object{
        private const val TAG = "MyServiceTAG"
    }
}
