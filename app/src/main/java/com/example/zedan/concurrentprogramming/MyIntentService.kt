package com.example.zedan.concurrentprogramming

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import androidx.core.app.JobIntentService
import com.example.zedan.concurrentprogramming.MainActivity.Companion.FILE_CONTENTS_KEY
import java.net.URL
import java.nio.charset.Charset

/**
 * An [JobIntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * helper methods.
 */
class MyIntentService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        if (intent.action == JOB_ACTION){
            val fileUrl = URL(intent.getStringExtra(EXTRA_FILE_URL))
            val contents = fileUrl.readText(Charset.defaultCharset())
            Log.i(TAG, "onHandleWork: $contents")

            val bundle = Bundle()
            bundle.putString(FILE_CONTENTS_KEY, contents)
            val receiver = intent.getParcelableExtra<ResultReceiver>(RECEIVER_KEY)
            receiver?.send(Activity.RESULT_OK, bundle)
        }
    }


    companion object {

        private const val TAG = "RunnerCodeIntentService"

        private const val JOB_ACTION = "com.example.zedan.concurrentprogramming.action.FOO"

        private const val EXTRA_FILE_URL = "com.example.zedan.concurrentprogramming.extra.FILE_URL"
        private const val RECEIVER_KEY = "receiver_key"

        private const val JOB_ID = 1001
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see JobIntentService
         */
        @JvmStatic
        fun startAction(context: Context, fileUrl: String, receiver: ResultReceiver) {
            val intent = Intent(context, MyIntentService::class.java).apply {
                action = JOB_ACTION
                putExtra(RECEIVER_KEY, receiver)
                putExtra(EXTRA_FILE_URL, fileUrl)
            }
            enqueueWork(context, MyIntentService::class.java, JOB_ID, intent)
        }
    }
}
