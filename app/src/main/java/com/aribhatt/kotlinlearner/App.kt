package com.aribhatt.kotlinlearner

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App: Application() {
    companion object {
        val CHANNEL_ID:String = "playgroundServiceChannel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotification()
    }
    private fun createNotification() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel: NotificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Playground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager: NotificationManager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}