package com.example.zedan.concurrentprogramming

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.URL
import java.nio.charset.Charset

class MyWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams){

    override suspend fun doWork(): Result {

        val contents = withContext(Dispatchers.IO){

            setProgress(workDataOf(MESSAGE_KEY to "Doing some work"))
            delay(1000)
            setProgress(workDataOf(MESSAGE_KEY to "Doing more some work"))
            delay(1000)
            setProgress(workDataOf(MESSAGE_KEY to "Almost done "))
            delay(1000)

            val url = URL(fileUrl)
            url.readText(Charset.defaultCharset())
        }

        val result = workDataOf(DATA_KEY to contents)
        return Result.success(result)
    }

    companion object {
        private const val TAG = "WorkerTAG"
        private const val fileUrl = "https://2833069.youcanlearnit.net/lorem_ipsum.txt"
        const val DATA_KEY = "data_key"
        const val MESSAGE_KEY = "message_key"
    }
}