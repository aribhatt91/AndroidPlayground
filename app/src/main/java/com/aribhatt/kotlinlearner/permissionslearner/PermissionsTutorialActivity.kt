package com.aribhatt.kotlinlearner.permissionslearner

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.aribhatt.kotlinlearner.R
import com.aribhatt.kotlinlearner.databinding.ActivityPermissionsTutorialBinding
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/*
* https://blog.mindorks.com/implementing-easy-permissions-in-android-android-tutorial#:~:text=In%20the%20API%20level%2023,for%20permission%20at%20runtime%20also.
* */
class PermissionsTutorialActivity : AppCompatActivity(),
    EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks{

    private val TAG = "PermissionsTutorialActivity"
    private val RC_CAMERA_PERM = 123
    private val RC_LOCATION_PERM = 124
    private val RC_SMS_PERM = 125
    private val RC_CONTACTS_PERM = 126

    private lateinit var binding: ActivityPermissionsTutorialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionsTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        binding.btnCamera.setOnClickListener{
            cameraTask()
        }
        binding.btnSms.setOnClickListener {
            smsTask()
        }
        binding.btnLocation.setOnClickListener {
            locationTask()
        }
        binding.btnContact.setOnClickListener{
            contactTask()
        }
    }

    private fun hasCameraPermission():Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)
    }
    private fun hasLocationPermissions():Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)
    }
    private fun hasSmsPermission():Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_SMS)
    }
    private fun hasContactPermission():Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS)
    }

    private fun cameraTask() {
        if (hasCameraPermission())
        {
            // Have permission, do the thing!
            Toast.makeText(this, "TODO: Camera things", Toast.LENGTH_LONG).show()
        }
        else
        {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_camera),
                RC_CAMERA_PERM,
                Manifest.permission.CAMERA)
        }
    }
    private fun locationTask() {
        if (hasLocationPermissions())
        {
            // Have permission, do the thing!
            Toast.makeText(this, "TODO: Location things", Toast.LENGTH_LONG).show()
        }
        else
        {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_location),
                RC_LOCATION_PERM,
                Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun smsTask() {
        if (hasSmsPermission())
        {
            Toast.makeText(this, "TODO: SMS things", Toast.LENGTH_LONG).show()
        }
        else
        {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_sms),
                RC_SMS_PERM,
                Manifest.permission.READ_SMS)
        }
    }

    private fun contactTask() {
        if (hasContactPermission())
        {
            Toast.makeText(this, "TODO: Contact things", Toast.LENGTH_LONG).show()
        }
        else
        {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_contact),
                RC_CONTACTS_PERM,
                Manifest.permission.READ_CONTACTS)
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
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(this, "onPermissionsGranted:" + requestCode + ":" + perms.size, Toast.LENGTH_LONG).show()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
        {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onRationaleAccepted(requestCode: Int) {
        Log.d(TAG, "onRationaleAccepted:" + requestCode)
        Toast.makeText(this, "onRationaleAccepted:" + requestCode, Toast.LENGTH_LONG).show()
    }

    override fun onRationaleDenied(requestCode: Int) {
        Log.d(TAG, "onRationaleDenied:" + requestCode)
        Toast.makeText(this, "onRationaleDenied:" + requestCode, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE)
        {
            val yes = getString(R.string.yes)
            val no = getString(R.string.no)
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                this,
                getString(R.string.returned_from_app_settings_to_activity,
                    if (hasCameraPermission()) yes else no,
                    if (hasLocationPermissions()) yes else no,
                    if (hasSmsPermission()) yes else no,
                    if (hasContactPermission()) yes else no),
                Toast.LENGTH_LONG)
                .show()
        }
    }
}