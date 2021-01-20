package com.aribhatt.kotlinlearner.service.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import android.widget.Toast
/*
* https://www.vogella.com/tutorials/AndroidBroadcastReceiver/article.html
* */
class VibrateReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Don't panic but your time is up", Toast.LENGTH_LONG).show()
        val v: Vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.vibrate(5000)
    }
}