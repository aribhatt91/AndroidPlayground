package com.aribhatt.kotlinlearner.activitytracker.utils
import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.REQUEST_CODE_LOCATIONS_PERMISSIONS
import pub.devrel.easypermissions.EasyPermissions

object Permissions {
    fun hasLocationPermissions(context: Context): Boolean {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }else {
            return EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }
    fun requestLocationPermissions(context: Activity) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            EasyPermissions.requestPermissions(
                context,
                "You need to accept location permissions to use this app",
                REQUEST_CODE_LOCATIONS_PERMISSIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }else {
            EasyPermissions.requestPermissions(
                context,
                "You need to accept location permissions to use this app",
                REQUEST_CODE_LOCATIONS_PERMISSIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }
}