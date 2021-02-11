package com.aribhatt.kotlinlearner.activitytracker

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.aribhatt.kotlinlearner.R
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.REQUEST_CODE_LOCATIONS_PERMISSIONS
import com.aribhatt.kotlinlearner.activitytracker.data.db.RunDAO
import com.aribhatt.kotlinlearner.activitytracker.service.BackgroundDetectedActivitiesService
import com.aribhatt.kotlinlearner.activitytracker.utils.Permissions
import com.aribhatt.kotlinlearner.activitytracker.utils.Utility
import com.aribhatt.kotlinlearner.databinding.ActivityTrackerBinding
import com.google.android.gms.location.DetectedActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.internal.InjectedFieldSignature
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

/*
https://developer.android.com/guide/topics/location/transitions
https://medium.com/@abhiappmobiledeveloper/android-activity-recognition-api-b7f61847d9dc
*/

/*
Running App Playlist
https://www.youtube.com/watch?v=XqkFTG10sRk&list=PLQkwcJG4YTCQ6emtoqSZS2FVwZR9FT3BV
*/
@AndroidEntryPoint
class TrackerActivity : AppCompatActivity(),
    EasyPermissions.PermissionCallbacks {
    private lateinit var binding: ActivityTrackerBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private val TAG = TrackerActivity::class.java.simpleName
    //internal lateinit var broadcastReceiver: BroadcastReceiver
    /*private lateinit var txtActivity: TextView
    private lateinit var txtConfidence: TextView
    private lateinit var btnStartTrcking: Button
    private lateinit var btnStopTracking: Button*/

    /*@Inject
    lateinit var runDAO: RunDAO*/

    companion object {
        val BROADCAST_DETECTED_ACTIVITY = "activity_intent"
        internal val DETECTION_INTERVAL_IN_MILLISECONDS: Long = 1000
        val CONFIDENCE = 70
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Activity Tracker"
        //setSupportActionBar(binding.toolbar)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        navigateToTrackingFragmentIfNeeded(intent)

        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.setOnNavigationItemReselectedListener {
            /*No action if the selected item is reselected, else it will reload the currently selected fragment*/
        }

        navController
            .addOnDestinationChangedListener { controller, destination, arguments ->
                when(destination.id) {
                    R.id.settingsFragment, R.id.runFragment, R.id.statsFragment -> {
                        binding.bottomNavigationView.visibility = View.VISIBLE
                    }
                    else -> binding.bottomNavigationView.visibility = View.GONE
                }
            }
        requestLocationPermission()
        //Log.i("TrackerActivity", "RUNDAO : ${runDAO.hashCode()}")
        /*binding.btnStartTracking.setOnClickListener {
            startTracking()
        }
        binding.btnStopTracking.setOnClickListener {
            stopTracking()
        }
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == BROADCAST_DETECTED_ACTIVITY) {
                    val type = intent.getIntExtra("type", -1)
                    val confidence = intent.getIntExtra("confidence", 0)
                    handleUserActivity(type, confidence)
                }
            }
        }*/
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !hasPermission()) {

            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location), 123, Manifest.permission.ACTIVITY_RECOGNITION)
            //ActivityCompat.requestPermissions(this, arrayOf("com.google.android.gms.permission.ACTIVITY_RECOGNITION"), 1)
        }
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(this, "com.google.android.gms.permission.ACTIVITY_RECOGNITION")
            != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission not granted")
            // Permission is not granted
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location), 124, "com.google.android.gms.permission.ACTIVITY_RECOGNITION")
        }else {
            Log.i(TAG, "Permission granted")
            startTracking()
        }*/

    }
    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        if(intent?.action === ACTION_SHOW_TRACKING_FRAGMENT) {
            navController.navigate(R.id.action_global_trackingFragment)
        }

    }
    private fun requestLocationPermission(){
        if(Permissions.hasLocationPermissions(this)){
            return
        }
        Permissions.requestLocationPermissions(this)
    }

    private fun hasPermission(): Boolean{
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACTIVITY_RECOGNITION)
    }

    private fun handleUserActivity(type: Int, confidence: Int) {
        var label = getString(R.string.activity_unknown)
        when (type) {
            DetectedActivity.IN_VEHICLE -> {
                label = "You are in Vehicle"
            }
            DetectedActivity.ON_BICYCLE -> {
                label = "You are on Bicycle"
            }
            DetectedActivity.ON_FOOT -> {
                label = "You are on Foot"
            }
            DetectedActivity.RUNNING -> {
                label = "You are Running"
            }
            DetectedActivity.STILL -> {
                label = "You are Still"
            }
            DetectedActivity.TILTING -> {
                label = "Your phone is Tilted"
            }
            DetectedActivity.WALKING -> {
                label = "You are Walking"
            }
            DetectedActivity.UNKNOWN -> {
                label = "Unkown Activity"
            }
        }
        //Log.e(TAG, "User activity: $label, Confidence: $confidence")
        if (confidence > CONFIDENCE) {
            /*binding.txtActivity.text = label
            binding.txtConfidence.text = "Confidence: $confidence"*/
        }
    }
    override fun onResume() {
        super.onResume()
        /*LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
            IntentFilter(BROADCAST_DETECTED_ACTIVITY)
        )*/
    }
    override fun onPause() {
        super.onPause()
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    private fun startTracking() {
        /*val intent = Intent(this@TrackerActivity, BackgroundDetectedActivitiesService::class.java)
        startService(intent)*/
    }
    private fun stopTracking() {
        //Qualified this - https://kotlinlang.org/docs/reference/this-expressions.html
        val intent = Intent(this@TrackerActivity, BackgroundDetectedActivitiesService::class.java)
        stopService(intent)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        startTracking()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}