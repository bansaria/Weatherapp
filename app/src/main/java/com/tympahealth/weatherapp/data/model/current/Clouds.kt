package com.tympahealth.weatherapp.data.model.current

import com.google.gson.annotations.SerializedName


data class Clouds (

  @SerializedName("all" ) var all : Int? = null

)