package com.barisic.weather.models

data class WeatherData(
    var id: Int,
    var description: String,
    var icon: String,
    var main: String
)