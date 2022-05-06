package com.tympahealth.weatherapp.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tympahealth.weatherapp.data.AppConstant
import com.tympahealth.weatherapp.data.model.current.CurrentWeatherData
import com.tympahealth.weatherapp.data.model.forecast.ForecastWeatherData
import com.tympahealth.weatherapp.data.repository.WeatherInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class WeatherInfoViewModel @Inject constructor(private val repository: WeatherInfoRepository) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable -> onError("Exception:: ${throwable.message}") }

    private val coroutinescope = CoroutineScope(Dispatchers.IO + exceptionHandler)

    var loaderLiveData = MutableLiveData<Boolean>()

    var errorLiveData = MutableLiveData<String>()

    var currentWeatherLiveData = MutableLiveData<CurrentWeatherData>()

    var forecastWeatherLiveData = MutableLiveData<ForecastWeatherData>()

    fun getCurrentWeatherInfo(latitude: String, longitude: String, appId: String) {

        loaderLiveData.value = true

        coroutinescope.launch {

            val response = repository.getCurrentWeatherInfo(latitude, longitude, appId, "metric")

            withContext(Dispatchers.Main) {

                if (response.isSuccessful) {
                    currentWeatherLiveData.value = response.body()
                    loaderLiveData.value = false
                } else {
                    onError("Error:: ${response.message()}")
                }
            }
        }
    }

    fun getForecastWeatherInfo(latitude: String, longitude: String, appId: String) {
        loaderLiveData.value = true

        coroutinescope.launch {

            val response = repository.getForecastWeatherInfo(latitude, longitude, appId, "metric")

            withContext(Dispatchers.Main) {

                if (response.isSuccessful) {
                    forecastWeatherLiveData.value = response.body()
                    loaderLiveData.value = false
                } else {
                    onError("Error:: ${response.message()}")
                }
            }
        }
    }

    private fun onError(message: String) {
        Log.d(AppConstant.LOG_TAG, "onError $message")

        loaderLiveData.postValue(false)
        errorLiveData.postValue(message)
    }

    override fun onCleared() {
        super.onCleared()
        coroutinescope.cancel()
    }
}