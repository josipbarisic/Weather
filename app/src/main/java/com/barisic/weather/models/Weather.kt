package com.barisic.weather.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.barisic.weather.R
import com.barisic.weather.util.*

@Entity(tableName = "current_weather")
data class Weather(
    var base: String,
    @Embedded(prefix = "clouds_")
    var clouds: Clouds,
    var cod: Int,
    @Embedded(prefix = "coord_")
    var coord: Coord,
    var dt: Int,
    @Embedded(prefix = "main_")
    var main: Main,
    var name: String,
    @Embedded(prefix = "sys_")
    var sys: Sys,
    var timezone: Int,
    var visibility: Int,
    var weather: List<WeatherData>,
    @Embedded(prefix = "wind_")
    var wind: Wind
) {
    @PrimaryKey(autoGenerate = false)
    var w_id: Int = CURRENT_DATA_ID

    fun getWeatherImgRes(): Int {
        return when (weather[0].main) {
            CLOUDS -> if (isDaytime()) R.drawable.cloudy_day else R.drawable.cloudy_night
            CLEAR -> if (isDaytime()) R.drawable.clear_day else R.drawable.clear_night
            RAIN, DRIZZLE -> if (isDaytime()) R.drawable.rainy_day else R.drawable.rainy_night
            THUNDERSTORM -> if (isDaytime()) R.drawable.thunderstorm_day else R.drawable.thunderstorm_night
            SNOW -> if (isDaytime()) R.drawable.snowy_day else R.drawable.snowy_night
            else -> R.drawable.mist
        }
    }

    fun getHours(): String {
        return Common.getHoursFromUnix(dt.toLong())
    }

    fun getDay(): String {
        return Common.getDayFromUnix(dt.toLong())
    }

    fun getDate(): String {
        return Common.getDateFromUnix(dt.toLong())
    }

    fun getWeatherData(): WeatherData {
        return weather[0]
    }

    fun getYoutubeQueryString(): String {
        return "${getWeatherData().main} $name|${getWeatherData().main} weather"
    }

    private fun isDaytime(): Boolean {
        return sys.sunrise < dt && dt < sys.sunset
    }
}