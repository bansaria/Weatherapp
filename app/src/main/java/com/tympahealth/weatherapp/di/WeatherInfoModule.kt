package com.tympahealth.weatherapp.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.tympahealth.weatherapp.data.api.WeatherInfoApi
import com.tympahealth.weatherapp.data.repository.WeatherInfoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WeatherInfoModule {

    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val httpInterceptor = HttpLoggingInterceptor()
        httpInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(httpInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient).build()
    }

    @Singleton
    @Provides
    fun provideWeatherInfoApi(retrofit: Retrofit): WeatherInfoApi = retrofit.create(WeatherInfoApi::class.java)

    @Singleton
    @Provides
    fun provideUserInfoRepository(apiService: WeatherInfoApi): WeatherInfoRepository = WeatherInfoRepository(apiService)

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("preferences_name", Context.MODE_PRIVATE)
    }
}