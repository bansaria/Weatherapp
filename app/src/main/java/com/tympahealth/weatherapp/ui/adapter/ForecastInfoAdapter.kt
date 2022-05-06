package com.tympahealth.weatherapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tympahealth.weatherapp.R
import com.tympahealth.weatherapp.data.model.forecast.List
import com.tympahealth.weatherapp.databinding.ItemForecastDetailsBinding
import com.tympahealth.weatherapp.util.Util

class ForecastInfoAdapter(private val context: Context, private val forecastList: MutableList<List>) : RecyclerView.Adapter<ForecastInfoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binder = ItemForecastDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(forecastList[position])
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    inner class ViewHolder(private val binder: ItemForecastDetailsBinding) : RecyclerView.ViewHolder(binder.root) {

        fun bind(data: List) {
            val date = data.dt ?: 0
            binder.tvDay.text = Util.convertUnixTimeDay(date)

            var humidity = 0

            data.main?.let { mainData ->

                mainData.temp?.let {
                    binder.tvCurrentTemp.text = "$it"
                }

                val tempMin = mainData.tempMin ?: 0
                val tempMax = mainData.tempMax ?: 0

                binder.tvCurrentMaxMinTemp.text = "($tempMax/$tempMin) ${context.getString(R.string.text_celsius)}"

                humidity = mainData.humidity ?: 0
            }

            val weather = data.weather
            val description = if (weather.size > 0) {
                weather[0].description
            } else {
                context.getString(R.string.text_no_data)
            }

            val weatherDescription = String.format(context.getString(R.string.text_weather_description), description, "$humidity%")
            binder.tvCurrentWeatherDescription.text = weatherDescription
        }
    }
}