package com.tympahealth.weatherapp.ui

import androidx.lifecycle.ViewModel
import com.tympahealth.weatherapp.data.repository.WeatherInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherInfoViewModel @Inject constructor(repository: WeatherInfoRepository) : ViewModel() {

    fun getCurrentWeatherInfo(latitude: String, longitude: String, appId: String) {

    }

    fun getForecastWeatherInfo(latitude: String, longitude: String, appId: String) {

    }
}