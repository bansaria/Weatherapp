package com.tympahealth.weatherapp.ui

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.tympahealth.weatherapp.data.AppConstant
import com.tympahealth.weatherapp.data.model.current.CurrentWeatherData
import com.tympahealth.weatherapp.data.model.forecast.ForecastWeatherData
import com.tympahealth.weatherapp.data.repository.WeatherInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class WeatherInfoViewModel @Inject constructor(private val repository: WeatherInfoRepository, private val preferences: SharedPreferences, private val gson: Gson) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable -> onError("Exception:: ${throwable.message}") }

    private val coroutinescope = CoroutineScope(Dispatchers.IO + exceptionHandler)

    var loaderLiveData = MutableLiveData<Boolean>()

    var errorLiveData = MutableLiveData<String>()

    var currentWeatherLiveData = MutableLiveData<CurrentWeatherData>()

    var forecastWeatherLiveData = MutableLiveData<ForecastWeatherData>()

    fun getCurrentWeatherInfo(locationName: String, appId: String) {

        loaderLiveData.value = true

        coroutinescope.launch {

            val response = repository.getCurrentWeatherInfo(locationName, appId, "metric")

            withContext(Dispatchers.Main) {

                if (response.isSuccessful) {
                    currentWeatherLiveData.value = response.body()
                    loaderLiveData.value = false
                    cacheWeatherData(AppConstant.SP_CURRENT_WEATHER_DATA, gson.toJson(response.body()))
                } else {
                    onError("Error:: ${response.message()}")
                }
            }
        }
    }

    fun getForecastWeatherInfo(locationName: String, appId: String) {
        loaderLiveData.value = true

        coroutinescope.launch {

            val response = repository.getForecastWeatherInfo(locationName, appId, "metric")

            withContext(Dispatchers.Main) {

                if (response.isSuccessful) {
                    forecastWeatherLiveData.value = response.body()
                    loaderLiveData.value = false
                    cacheWeatherData(AppConstant.SP_FORECAST_WEATHER_DATA, gson.toJson(response.body()))
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

    private fun cacheWeatherData(tag: String, data: String) {
        Log.d(AppConstant.LOG_TAG, "cacheWeatherData $tag $data")
        val edit = preferences.edit()
        edit.putString(tag, data)
        edit.commit()
    }

    fun getCacheData(tag: String) {
        val json = preferences.getString(tag, "{}")
        when (tag) {
            AppConstant.SP_CURRENT_WEATHER_DATA -> {
                val data = gson.fromJson(json, CurrentWeatherData::class.java)
                currentWeatherLiveData.postValue(data)
            }
            AppConstant.SP_FORECAST_WEATHER_DATA -> {
                val data = gson.fromJson(json, ForecastWeatherData::class.java)
                forecastWeatherLiveData.postValue(data)
            }
        }
        loaderLiveData.value = false
    }

    override fun onCleared() {
        super.onCleared()
        coroutinescope.cancel()
    }
}