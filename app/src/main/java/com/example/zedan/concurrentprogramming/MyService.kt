package com.example.zedan.concurrentprogramming

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class MyService : Service() {

    private val binder = MyBindService()

    private lateinit var player: MediaPlayer

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action){
            NOTIFICATION_ACTION_PLAY -> startMusic()
            NOTIFICATION_ACTION_STOP -> stopMusic()
        }
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String{
        val channelId = "my_service"
        val channelName = "Music Service"
        NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT).also {
            it.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as
                    NotificationManager

            notificationManager.createNotificationChannel(it)
        }

        return channelId
    }



    private fun displayForegroundNotification(){
        val channelId =
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                createNotificationChannel()
            }else{
                ""
            }
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val playIntent = getPendingIntent(NOTIFICATION_ACTION_PLAY)
        val stopIntent = getPendingIntent(NOTIFICATION_ACTION_STOP)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Playing music")
            .setContentText(AUDIO_FILE)
            .setSmallIcon(R.drawable.ic_run)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(0, "Play", playIntent)
            .addAction(0, "Stop", stopIntent)
            .setWhen(0)
            .build()

        startForeground(1001, notification)
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

        displayForegroundNotification()

    }

    fun stopMusic(){
        try {
            player.stop()
        }catch (e: UninitializedPropertyAccessException){
        }

        stopForeground(false)

    }

    private fun getPendingIntent(action: String): PendingIntent{
        val serviceIntent = Intent(this, MyService::class.java).also {
            it.action = action
        }
        return PendingIntent.getService(this, 0, serviceIntent, 0)
    }

    inner class MyBindService : Binder(){
        fun getService() = this@MyService
    }

    companion object{
        private const val TAG = "MyServiceTAG"
        const val AUDIO_FILE = "happy_day.mp3"

        private const val NOTIFICATION_ACTION_PLAY = "action_play"
        private const val NOTIFICATION_ACTION_STOP = "action_stop"

    }
}
