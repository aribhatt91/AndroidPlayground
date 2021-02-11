package com.aribhatt.kotlinlearner.common.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log

/*
* https://www.vogella.com/tutorials/AndroidBroadcastReceiver/article.html
* */
//This broadcast receiver declares an intent-filter for a protected broadcast action string, which can only be sent by the system, not third-party applications. However, the receiver's onReceive method does not appear to call getAction to ensure that the received Intent's action string matches the expected value, potentially making it possible for another actor to send a spoofed intent with no action string or a different action string and cause undesired behavior.
class PhoneReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val extras: Bundle? = intent?.extras

        if(extras != null) {
            val state = extras.getString(TelephonyManager.EXTRA_STATE) ?: ""
            Log.w("MY_DEBUG_TAG", state)
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                val phoneNumber = extras
                        .getString(TelephonyManager.EXTRA_INCOMING_NUMBER) ?: ""
                Log.w("MY_DEBUG_TAG", phoneNumber)
            }
        }
    }
}