package com.tympahealth.weatherapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tympahealth.weatherapp.R
import com.tympahealth.weatherapp.data.AppConstant
import com.tympahealth.weatherapp.data.model.current.CurrentWeatherData
import com.tympahealth.weatherapp.data.model.forecast.ForecastWeatherData
import com.tympahealth.weatherapp.databinding.FragmentWeatherInfoBinding
import com.tympahealth.weatherapp.ui.adapter.ForecastInfoAdapter
import com.tympahealth.weatherapp.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherInfoFragment : Fragment() {

    private lateinit var viewModel: WeatherInfoViewModel

    private lateinit var activity: MainActivity

    private lateinit var binding: FragmentWeatherInfoBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this)[WeatherInfoViewModel::class.java]
        attachObserver()
        binding = FragmentWeatherInfoBinding.inflate(layoutInflater, container, false)

        addListener()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        fetchWeatherInfo()
    }

    private fun addListener() {
        binding.swipeLayout.setOnRefreshListener {
            fetchWeatherInfo()
        }
    }

    private fun fetchWeatherInfo() {
        if (Util.isNetworkAvailable(activity)) {
            viewModel.getCurrentWeatherInfo(AppConstant.LOCATION, AppConstant.API_ID)
            viewModel.getForecastWeatherInfo(AppConstant.LOCATION, AppConstant.API_ID)
        } else {
            binding.swipeLayout.isRefreshing = false
            viewModel.getCacheData(AppConstant.SP_CURRENT_WEATHER_DATA)
            viewModel.getCacheData(AppConstant.SP_FORECAST_WEATHER_DATA)
            activity.showError(getString(R.string.text_no_connectivity))
        }
    }

    private fun attachObserver() {
        viewModel.loaderLiveData.observe(viewLifecycleOwner) { isLoader ->
            if (isLoader) activity.showLoading() else {
                activity.hideloading()
                binding.swipeLayout.isRefreshing = false
            }
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { message ->
            activity.showError(message)
        }

        viewModel.currentWeatherLiveData.observe(viewLifecycleOwner) { data ->
            updateUi(data)
        }

        viewModel.forecastWeatherLiveData.observe(viewLifecycleOwner) { data ->
            updateForecastData(data)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(data: CurrentWeatherData) {
        val cod = data.cod ?: -1
        if (cod > 0) {
            val date = data.dt ?: 0
            binding.tvRefreshTime.text = Util.convertUnixTimeLocalTime(date)

            val location = data.name ?: getString(R.string.text_no_data)
            binding.tvLocation.text = location

            var humidity = 0

            data.main?.let { mainData ->

                mainData.temp?.let {
                    binding.tvCurrentTemp.text = "$it"
                }

                val tempMin = mainData.tempMin ?: 0
                val tempMax = mainData.tempMax ?: 0

                binding.tvCurrentMaxMinTemp.text = "($tempMax/$tempMin) ${getString(R.string.text_celsius)}"

                humidity = mainData.humidity ?: 0
            }

            val weather = data.weather
            val description = if (weather.size > 0) {
                weather[0].description
            } else {
                getString(R.string.text_no_data)
            }

            val weatherDescription = String.format(getString(R.string.text_weather_description), description, "$humidity%")
            binding.tvCurrentWeatherDescription.text = weatherDescription
            updateDataAvailableUi()
        } else {
            Log.d(AppConstant.LOG_TAG, "No data available to show")
            updateNoDataUi()
        }
    }

    private fun updateForecastData(data: ForecastWeatherData) {
        val cod = data.cod ?: ""
        if (cod.isNotEmpty()) {
            var adapter = ForecastInfoAdapter(activity, data.list)
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)
            binding.recyclerView.adapter = adapter
            updateDataAvailableUi()
        } else {
            Log.d(AppConstant.LOG_TAG, "No data available to show")
            updateNoDataUi()
        }
    }

    private fun updateNoDataUi() {
        binding.rlCurrentData.visibility = View.GONE
        binding.rlForecastData.visibility = View.GONE
        binding.tvNoData.visibility = View.VISIBLE
        binding.tvNoData.text = "${getString(R.string.text_no_data)} \n ${getString(R.string.text_no_connectivity)}"
    }

    private fun updateDataAvailableUi() {
        binding.rlCurrentData.visibility = View.VISIBLE
        binding.rlForecastData.visibility = View.VISIBLE
        binding.tvNoData.visibility = View.GONE
        binding.tvNoData.text = "${getString(R.string.text_no_data)} \n ${getString(R.string.text_no_connectivity)}"
    }
}