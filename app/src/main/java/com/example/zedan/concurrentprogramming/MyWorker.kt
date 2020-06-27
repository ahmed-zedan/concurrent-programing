package com.example.zedan.concurrentprogramming

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.net.URL
import java.nio.charset.Charset

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams){

    override fun doWork(): Result {

        Log.i(TAG, "doWork: is run")

        val url = URL(fileUrl)
        val contents = url.readText(Charset.defaultCharset())
        val result = workDataOf(DATA_KEY to contents)

        return Result.success(result)
    }

    companion object {
        private const val TAG = "WorkerTAG"
        private const val fileUrl = "https://2833069.youcanlearnit.net/lorem_ipsum.txt"
        const val DATA_KEY = "data_key"
    }
}