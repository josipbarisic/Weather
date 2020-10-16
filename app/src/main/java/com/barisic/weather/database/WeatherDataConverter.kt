package com.barisic.weather.database

import androidx.room.TypeConverter
import com.barisic.weather.models.WeatherData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherDataConverter {
    @TypeConverter
    fun fromList(list: List<WeatherData>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toList(string: String): List<WeatherData> {
        val type = object : TypeToken<List<WeatherData>>() {}.type
        return Gson().fromJson(string, type)
    }
}