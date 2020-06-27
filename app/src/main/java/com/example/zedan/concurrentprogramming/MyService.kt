package com.example.zedan.concurrentprogramming

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyService : Service() {

    private val binder = MyBindService()

    private lateinit var player: MediaPlayer

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    fun startMusic(){
        try {
            player.stop()
            player.release()
        }catch (e : UninitializedPropertyAccessException) {
        }

        player = MediaPlayer().also {
            assets.openFd(AUDIO_FILE).use {asset ->
                it.setDataSource(asset.fileDescriptor, asset.startOffset, asset.length)
            }
            it.prepare()
            it.start()
        }

    }

    fun stopMusic(){
        try {
            player.stop()
        }catch (e: UninitializedPropertyAccessException){
        }

    }

    inner class MyBindService : Binder(){
        fun getService() = this@MyService
    }

    companion object{
        private const val TAG = "MyServiceTAG"
        const val AUDIO_FILE = "happy_day.mp3"
    }
}
