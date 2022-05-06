package com.tympahealth.weatherapp.data.repository

import com.tympahealth.weatherapp.data.api.WeatherInfoApi
import javax.inject.Inject

class WeatherInfoRepository @Inject constructor(private val apiService: WeatherInfoApi) {

    suspend fun getCurrentWeatherInfo(locationName: String, appId: String, units: String) = apiService.getCurrentWeatherInfo(locationName, appId, units)

    suspend fun getForecastWeatherInfo(locationName: String, appId: String, units: String) = apiService.getForecastWeatherInfo(locationName, appId, units)
}