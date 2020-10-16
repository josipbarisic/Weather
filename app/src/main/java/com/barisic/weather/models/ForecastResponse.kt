package com.barisic.weather.models

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("list")
    val forecast: ArrayList<Weather>
)