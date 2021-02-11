package com.aribhatt.kotlinlearner.activitytracker.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import com.aribhatt.kotlinlearner.activitytracker.TrackerActivity
import com.google.android.gms.location.ActivityRecognitionClient
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity

class BackgroundDetectedActivitiesService : Service() {

    private lateinit var mIntentService: Intent
    private lateinit var mPendingIntent: PendingIntent
    private lateinit var mActivityRecognitionClient: ActivityRecognitionClient


    companion object {
        private val TAG = BackgroundDetectedActivitiesService::class.java.getSimpleName()
    }

    internal var mBinder: IBinder = LocalBinder()

    inner class LocalBinder: Binder() {
        val serverInstance: BackgroundDetectedActivitiesService
        get() = this@BackgroundDetectedActivitiesService
    }

    override fun onCreate() {
        super.onCreate()
        mActivityRecognitionClient = ActivityRecognitionClient(this)
        mIntentService = Intent(this, DetectedActivityIntentService::class.java)
        mPendingIntent = PendingIntent.getService(this, 1, mIntentService, PendingIntent.FLAG_UPDATE_CURRENT)
        requestActivityUpdatesButtonHandler()
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return Service.START_STICKY
    }
    fun requestActivityUpdatesButtonHandler() {
        val task = mActivityRecognitionClient?.requestActivityUpdates(
            TrackerActivity.DETECTION_INTERVAL_IN_MILLISECONDS,
            mPendingIntent)
        task?.addOnSuccessListener {
            Toast.makeText(applicationContext,
                "Successfully requested activity updates",
                Toast.LENGTH_SHORT)
                .show()
        }
        task?.addOnFailureListener {
            Toast.makeText(applicationContext,
                "Requesting activity updates failed to start",
                Toast.LENGTH_SHORT)
                .show()
        }
    }
    fun removeActivityUpdatesButtonHandler() {
        val task = mActivityRecognitionClient?.removeActivityUpdates(
            mPendingIntent)
        task?.addOnSuccessListener {
            Toast.makeText(applicationContext,
                "Removed activity updates successfully!",
                Toast.LENGTH_SHORT)
                .show()
        }
        task?.addOnFailureListener {
            Toast.makeText(applicationContext, "Failed to remove activity updates!",
                Toast.LENGTH_SHORT).show()
        }
    }
    fun requestActivityTransitionUpdatesButtonHandler() {
        val transitions = mutableListOf<ActivityTransition>()

        transitions +=
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()

        transitions +=
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()
        transitions +=
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.RUNNING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
        transitions +=
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.RUNNING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()

        transitions +=
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
        transitions +=
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()
        transitions +=
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_BICYCLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
        transitions +=
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_BICYCLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()
        transitions +=
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
        transitions +=
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()

        val task = mActivityRecognitionClient?.requestActivityTransitionUpdates(
            ActivityTransitionRequest(transitions),
            mPendingIntent
        )
        task?.addOnSuccessListener {
            Toast.makeText(applicationContext,
                "Successfully requested activity transition updates",
                Toast.LENGTH_SHORT)
                .show()
        }
        task?.addOnFailureListener {
            Toast.makeText(applicationContext,
                "Requesting activity transition updates failed to start",
                Toast.LENGTH_SHORT)
                .show()
        }
    }
    fun removeActivityTransitionUpdatesButtonHandler() {
        val task = mActivityRecognitionClient?.removeActivityTransitionUpdates(mPendingIntent)
        task?.addOnSuccessListener {
            Toast.makeText(applicationContext,
                "Removed activity transition updates successfully!",
                Toast.LENGTH_SHORT)
                .show()
        }
        task?.addOnFailureListener {
            Toast.makeText(applicationContext, "Failed to remove activity transition updates!",
                Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        removeActivityUpdatesButtonHandler()
    }

}