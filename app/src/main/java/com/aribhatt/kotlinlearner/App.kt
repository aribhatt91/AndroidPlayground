package com.aribhatt.kotlinlearner

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

//Tell the app that we want to inject dependencies using Dagger Hilt
@HiltAndroidApp
class App: Application() {
    companion object {
        val CHANNEL_ID:String = "playgroundServiceChannel"
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
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