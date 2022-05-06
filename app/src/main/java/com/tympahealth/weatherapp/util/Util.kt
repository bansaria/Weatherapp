package com.tympahealth.weatherapp.util

import android.content.Context
import android.net.ConnectivityManager
import java.net.InetAddress
import java.text.SimpleDateFormat
import java.util.*


object Util {

    fun convertUnixTimeLocalTime(unixTime: Int): String {
        val timelineMilliSec = unixTime.toLong() * 1000
        val df: Date = Date(timelineMilliSec)
        return SimpleDateFormat("dd,MMM,yyyy hh:mma", Locale.ENGLISH).format(df)
    }

    fun convertUnixTimeDay(unixTime: Int): String {
        val timelineMilliSec = unixTime.toLong() * 1000
        val df: Date = Date(timelineMilliSec) //return SimpleDateFormat("EEE dd,MMM", Locale.ENGLISH).format(df)
        return convertUnixTimeLocalTime(unixTime)
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo

        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}