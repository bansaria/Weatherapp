package com.tympahealth.weatherapp.util

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
}