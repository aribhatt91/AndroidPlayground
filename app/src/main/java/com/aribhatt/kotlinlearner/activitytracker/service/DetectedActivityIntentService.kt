package com.aribhatt.kotlinlearner.activitytracker.service

import android.app.IntentService
import android.content.Intent
import android.content.Context
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.aribhatt.kotlinlearner.activitytracker.ui.TrackerActivity
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity

// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_FOO = "com.aribhatt.kotlinlearner.activitytracker.service.action.FOO"
private const val ACTION_BAZ = "com.aribhatt.kotlinlearner.activitytracker.service.action.BAZ"

// TODO: Rename parameters
private const val EXTRA_PARAM1 = "com.aribhatt.kotlinlearner.activitytracker.service.extra.PARAM1"
private const val EXTRA_PARAM2 = "com.aribhatt.kotlinlearner.activitytracker.service.extra.PARAM2"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.

 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.

 */
class DetectedActivityIntentService : IntentService("DetectedActivityIntentService") {

    companion object {
        val TAG = DetectedActivityIntentService::class.java.simpleName
    }

    override fun onHandleIntent(intent: Intent?) {

        val result = ActivityRecognitionResult.extractResult(intent)
        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.
        val detectedActivities = result.probableActivities as ArrayList<*>
        for (activity in detectedActivities) {
            broadcastActivity(activity as DetectedActivity)
        }
    }

    private fun broadcastActivity(activity: DetectedActivity) {
        val intent = Intent(TrackerActivity.BROADCAST_DETECTED_ACTIVITY)
        intent.putExtra("type", activity.type)
        intent.putExtra("confidence", activity.confidence)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}