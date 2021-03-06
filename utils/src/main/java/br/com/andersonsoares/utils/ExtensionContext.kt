package br.com.andersonsoares.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.res.Resources
import android.util.DisplayMetrics





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

fun Context.dpToPx(dp: Float): Float {
    return dp * this.resources.displayMetrics.density
}

fun Context.pxToDp(px: Float): Float {
    return px / this.resources.displayMetrics.density
}

fun Context.dpToPxInt(dp: Float): Float {
    return (dpToPx(dp) + 0.5f).toInt().toFloat()
}

fun Context.pxToDpCeilInt(px: Float): Float {
    return (pxToDp(px) + 0.5f).toInt().toFloat()
}