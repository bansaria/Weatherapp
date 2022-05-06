package com.tympahealth.weatherapp.data.api

import com.google.gson.JsonObject
import com.tympahealth.weatherapp.data.model.current.CurrentWeatherData
import com.tympahealth.weatherapp.data.model.forecast.ForecastWeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInfoApi {

    @GET("weather")
    suspend fun getCurrentWeatherInfo(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") appid: String, @Query("units") units: String): Response<CurrentWeatherData>

    @GET("forecast")
    suspend fun getForecastWeatherInfo(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") appid: String): Response<ForecastWeatherData>
}