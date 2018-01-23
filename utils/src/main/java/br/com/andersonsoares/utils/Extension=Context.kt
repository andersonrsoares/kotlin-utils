package br.com.andersonsoares.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE



/**
 * Created by andersonsoares on 23/01/2018.
 */


var TYPE_WIFI = 1
var TYPE_MOBILE = 2
var TYPE_NOT_CONNECTED = 0

@SuppressLint("MissingPermission")
fun Context.getConnectivityStatus(): Int{

    val cm = this
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val activeNetwork = cm.activeNetworkInfo
    if (null != activeNetwork) {
        if (activeNetwork.type == ConnectivityManager.TYPE_WIFI)
            return TYPE_WIFI

        if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
            return TYPE_MOBILE
    }
    return TYPE_NOT_CONNECTED
}

@SuppressLint("MissingPermission")
fun Context.isConnected(): Boolean {
    val cm = this
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val activeNetwork = cm.activeNetworkInfo
    if (null != activeNetwork) {
        if (activeNetwork.type == ConnectivityManager.TYPE_WIFI)
            return true

        if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
            return true
    }
    return false
}

