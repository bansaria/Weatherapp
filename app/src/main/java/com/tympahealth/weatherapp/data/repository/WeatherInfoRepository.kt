package com.tympahealth.weatherapp.data.repository

import com.tympahealth.weatherapp.data.api.WeatherInfoApi
import javax.inject.Inject

class WeatherInfoRepository @Inject constructor(private val apiService: WeatherInfoApi) {

    fun getCurrentWeatherInfo(latitude: String, longitude: String) {

    }

    fun getForecastWeatherInfo(latitude: String, longitude: String) {

    }
}