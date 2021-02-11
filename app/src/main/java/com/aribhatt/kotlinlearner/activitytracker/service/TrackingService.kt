package com.aribhatt.kotlinlearner.activitytracker.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.aribhatt.kotlinlearner.R
import com.aribhatt.kotlinlearner.activitytracker.TrackerActivity
import com.aribhatt.kotlinlearner.activitytracker.data.Constants
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.ACTION_PAUSE_SERVICE
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.ACTION_START_OR_RESUME_SERVICE
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.ACTION_STOP_SERVICE
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.FASTEST_LOCATION_INTERVAL
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.LOCATION_UPDATE_INTERVAL
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.NOTIFICATION_CHANNEL_ID
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.NOTIFICATION_CHANNEL_NAME
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.NOTIFICATION_ID
import com.aribhatt.kotlinlearner.activitytracker.utils.Permissions
import com.aribhatt.kotlinlearner.activitytracker.utils.Utility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


typealias PolyLine = MutableList<LatLng>
typealias PolyLines = MutableList<PolyLine>

@AndroidEntryPoint
class TrackingService: LifecycleService() {
    var isFirstRun = true

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    lateinit var currNotificationBuilder: NotificationCompat.Builder

    private val timeRunInSeconds = MutableLiveData<Long>()
    private var isTimerEnabled = false
    private var timeStarted = 0L
    private var lastSecondTimeStamp = 0L
    private var timeRun = 0L
    private var lapTime = 0L
    private var serviceKilled: Boolean = false

    companion object {
        val timeRunInMillis = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<PolyLines>()
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if(isTracking.value!!){
                result?.locations?.let { locations ->
                    for (location in locations) {
                        addPathPoint(location)
                        Timber.d("New location: ${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }
    }
    //https://www.youtube.com/watch?v=LTUmtp7IDEg&list=PLQkwcJG4YTCQ6emtoqSZS2FVwZR9FT3BV&index=15
    private fun startTimer() {
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true

        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking?.value!!) {
                /*Time dfference between now and time started*/
                lapTime = System.currentTimeMillis() - timeStarted

                timeRunInMillis.postValue(timeRun + lapTime)

                if(timeRunInMillis.value!! >= lastSecondTimeStamp + 1000L) {
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    lastSecondTimeStamp += 1000L
                }
                delay(50L)
            }

            timeRun += lapTime
        }
    }

    private fun pauseService(){
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    private fun updateNotificationTrackingState(isTracking: Boolean) {
        var notificationText = if(isTracking) "Pause" else "Resume"
        val pendingIntent = if(isTracking){
            val pauseIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1, pauseIntent, FLAG_UPDATE_CURRENT)
        } else {
            val resumeIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_START_OR_RESUME_SERVICE
            }
            PendingIntent.getService(this, 1, resumeIntent, FLAG_UPDATE_CURRENT)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        /*TODO: Can this be improved?*/
        //https://www.youtube.com/watch?v=OE6wB_MHmgA&list=PLQkwcJG4YTCQ6emtoqSZS2FVwZR9FT3BV&index=16
        currNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(currNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }
        currNotificationBuilder = notificationBuilder
            .addAction(R.drawable.ic_baseline_pause_black, notificationText, pendingIntent)
        notificationManager.notify(NOTIFICATION_ID, currNotificationBuilder.build())
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean){
        if(isTracking){
            if(Permissions.hasLocationPermissions(this)){
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY

                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }else {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }
    }

    fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
    }
    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(it.latitude, it.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }
    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        currNotificationBuilder = notificationBuilder
        //Injected using Dagger-Hilt
        /*fusedLocationProviderClient = FusedLocationProviderClient(this)*/

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
            updateNotificationTrackingState(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if(isFirstRun){
                        startForegroundService()
                        isFirstRun = false
                    }else {
                        startTimer()
                        Timber.d("Resumed service")
                    }

                }
                ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped service")
                }
                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                    pauseService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        startTimer()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        /*val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Tracker App")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())*/

        startForeground(NOTIFICATION_ID, notificationBuilder.build())

        timeRunInSeconds.observe(this,
        Observer {
            if (!serviceKilled){
                val notification = currNotificationBuilder.setContentText(
                    Utility.getFormattedStopwatchTime(it*1000L)
                )
                notificationManager.notify(NOTIFICATION_ID, notification.build())
            }

        })
        Timber.d("Started service")
    }

    fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, TrackerActivity::class.java).also {
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        },
        FLAG_UPDATE_CURRENT
    )

    private fun killService(){
        serviceKilled = true
        isFirstRun = true
        pauseService()
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}