package com.tympahealth.weatherapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherInfoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}