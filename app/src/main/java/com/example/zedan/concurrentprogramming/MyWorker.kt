package com.example.zedan.concurrentprogramming

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams){

    override fun doWork(): Result {

        Log.i(TAG, "doWork: is run")

        return Result.success()
    }

    companion object {
        private const val TAG = "WorkerTAG"
    }
}