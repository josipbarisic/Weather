package com.barisic.weather.models

import kotlin.math.roundToInt

data class Main(
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
) {
    fun getFeelsLikeTemp(): String = "$feels_like\u00B0"

    fun getHumidityString(): String = "$humidity %"

    fun getPressureString(): String = "$pressure hPa"

    fun getTemperature(): String = temp.roundToInt().toString()

    fun getTempWithDegree(): String = "${temp.roundToInt()}\u00B0"

    fun getMinMaxTemp(): String = "$temp_max\u00B0 / $temp_min\u00B0"
}