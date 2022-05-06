package com.tympahealth.weatherapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tympahealth.weatherapp.R
import com.tympahealth.weatherapp.data.AppConstant
import com.tympahealth.weatherapp.data.model.current.CurrentWeatherData
import com.tympahealth.weatherapp.databinding.FragmentWeatherInfoBinding
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
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getCurrentWeatherInfo("19.8762", "75.3433", AppConstant.API_ID)
        viewModel.getForecastWeatherInfo("19.8762", "75.3433", AppConstant.API_ID)
    }

    private fun attachObserver() {
        viewModel.loaderLiveData.observe(viewLifecycleOwner) { isLoader ->
            if (isLoader) activity.showLoading() else activity.hideloading()
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { message ->
            activity.showError(message)
        }

        viewModel.currentWeatherLiveData.observe(viewLifecycleOwner) { data ->
            updateUi(data)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(data: CurrentWeatherData) {

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
    }
}