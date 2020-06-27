package com.example.zedan.concurrentprogramming

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.util.Log
import androidx.core.app.JobIntentService

/**
 * An [JobIntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * helper methods.
 */
class MyIntentService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        when (intent.action) {
            ACTION_FOO -> {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)
                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                handleActionFoo(param1, param2)
            }
            ACTION_BAZ -> {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)
                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                handleActionBaz(param1, param2)
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFoo(param1: String, param2: String) {
        Log.i(TAG, "handleActionFoo: $param1 , $param2")
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionBaz(param1: String, param2: String) {
        Log.i(TAG, "handleActionBaz: $param1 , $param2")
    }

    companion object {

        private const val TAG = "RunnerCodeIntentService"

        // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
        private const val ACTION_FOO = "com.example.zedan.concurrentprogramming.action.FOO"
        private const val ACTION_BAZ = "com.example.zedan.concurrentprogramming.action.BAZ"

        private const val EXTRA_PARAM1 = "com.example.zedan.concurrentprogramming.extra.PARAM1"
        private const val EXTRA_PARAM2 = "com.example.zedan.concurrentprogramming.extra.PARAM2"

        private const val JOB_ID = 1001
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun startActionFoo(context: Context, param1: String, param2: String) {
            val intent = Intent(context, MyIntentService::class.java).apply {
                action = ACTION_FOO
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            enqueueWork(context, MyIntentService::class.java, JOB_ID, intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun startActionBaz(context: Context, param1: String, param2: String) {
            val intent = Intent(context, MyIntentService::class.java).apply {
                action = ACTION_BAZ
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            enqueueWork(context, MyIntentService::class.java, JOB_ID, intent)
        }
    }
}
