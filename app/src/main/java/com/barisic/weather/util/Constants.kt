package com.barisic.weather.util

//Networking
const val OWM_RETROFIT = "owm_retrofit"
const val GOOGLE_RETROFIT = "google_retrofit"
const val BASE_API_URL = "http://api.openweathermap.org/data/2.5/"
const val ICON_URL = "http://openweathermap.org/img/wn/"
const val YOUTUBE_SEARCH_URL = "https://www.googleapis.com/youtube/v3/"

//REQUEST CODES
const val AUTOCOMPLETE_REQUEST_CODE = 2510

//DATABASE
const val DATABASE_NAME = "db_weather"
const val CURRENT_DATA_ID = 0

//SHARED PREFS
const val SHARED_PREFS = "weather_prefs"
const val UPDATE_TIME = "update_time"

//DEFAULT VALUES
const val LAT = 51.509865
const val LNG = -0.118092
const val UPDATE_INTERVAL = 900

//EXTRA_KEY
const val VIDEO_ID = "video_id"

//SEARCH TYPE
const val WEATHER_ST = "weather?"
const val FORECAST_ST = "forecast?exclude=current,minutely,hourly,alerts&"

//COUNT
const val CNT_CURRENT = 1
const val CNT_FORECAST = 40

//UNITS
const val STANDARD_UNITS = "standard"
const val METRIC_UNITS = "metric"
const val IMPERIAL_UNITS = "imperial"

//WEATHER CONST
const val CLEAR = "Clear"
const val CLOUDS = "Clouds"
const val SNOW = "Snow"
const val RAIN = "Rain"
const val DRIZZLE = "Drizzle"
const val THUNDERSTORM = "Thunderstorm"