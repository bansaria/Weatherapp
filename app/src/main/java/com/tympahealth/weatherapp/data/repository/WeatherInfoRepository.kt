package com.tympahealth.weatherapp.data.repository

import com.tympahealth.weatherapp.data.api.WeatherInfoApi
import javax.inject.Inject

class WeatherInfoRepository @Inject constructor(private val apiService: WeatherInfoApi) {

    suspend fun getCurrentWeatherInfo(latitude: String, longitude: String, appId: String) = apiService.getCurrentWeatherInfo(latitude, longitude, appId)

    suspend fun getForecastWeatherInfo(latitude: String, longitude: String, appId: String) = apiService.getForecastWeatherInfo(latitude, longitude, appId)
}