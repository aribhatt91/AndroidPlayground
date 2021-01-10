package com.aribhatt.kotlinlearner.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager

class NetworkHelper {
    companion object {
        fun isNetworkAvailable(app: Application):Boolean {
            val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo?.isConnected ?: false
        }
    }
}