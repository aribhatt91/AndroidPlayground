package com.aribhatt.kotlinlearner.alarm.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import com.aribhatt.kotlinlearner.R
import com.aribhatt.kotlinlearner.databinding.ActivityAlarmBinding
import com.aribhatt.kotlinlearner.service.broadcastreceiver.VibrateReceiver

class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            cta.setOnClickListener {
                startAlarm(it)
            }
        }
    }

    private fun startAlarm(view: View) {
        val input = (binding.editText.text.toString() ?: "").toInt() ?: 0
        val intent: Intent = Intent(this, VibrateReceiver::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(applicationContext, 234324243, intent, 0)
        val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.currentThreadTimeMillis() + (input * 1000), pendingIntent)
        Toast.makeText(this, "Alarm set in $input seconds", Toast.LENGTH_LONG).show()
    }
}