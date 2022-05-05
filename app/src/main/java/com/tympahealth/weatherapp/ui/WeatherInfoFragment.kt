package com.tympahealth.weatherapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tympahealth.weatherapp.R
import com.tympahealth.weatherapp.data.AppConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherInfoFragment : Fragment() {

    private lateinit var viewModel: WeatherInfoViewModel

    private lateinit var activity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this)[WeatherInfoViewModel::class.java]
        attachObserver()
        return inflater.inflate(R.layout.fragment_weather_info, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getCurrentWeatherInfo("19.8762", "75.3433", AppConstant.API_ID)
    }

    private fun attachObserver() {
        viewModel.loaderLiveData.observe(viewLifecycleOwner) { isLoader ->
            if (isLoader) activity.showLoading() else activity.hideloading()
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { message ->
            activity.showError(message)
        }
    }
}