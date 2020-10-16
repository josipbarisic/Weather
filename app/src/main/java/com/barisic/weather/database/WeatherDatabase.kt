package com.barisic.weather.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.barisic.weather.models.CurrentLocation
import com.barisic.weather.models.Weather

@Database(
    entities = [Weather::class, CurrentLocation::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(WeatherDataConverter::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract val currentWeatherDAO: CurrentWeatherDAO
    abstract val currentLocationDAO: CurrentLocationDAO
}