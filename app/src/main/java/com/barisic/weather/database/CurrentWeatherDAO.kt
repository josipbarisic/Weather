package com.barisic.weather.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.barisic.weather.models.Weather
import com.barisic.weather.util.CURRENT_DATA_ID

@Dao
interface CurrentWeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCurrentWeather(weather: Weather): Long

    @Query("SELECT * FROM current_weather WHERE w_id=$CURRENT_DATA_ID")
    fun getCurrentWeather(): LiveData<Weather>
}