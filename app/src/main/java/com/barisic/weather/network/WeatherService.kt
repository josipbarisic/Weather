package com.barisic.weather.network

import com.barisic.weather.models.ForecastResponse
import com.barisic.weather.models.Weather
import com.barisic.weather.util.FORECAST_ST
import com.barisic.weather.util.WEATHER_ST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface WeatherService {
    @GET(WEATHER_ST)
    suspend fun getCurrentWeatherForLocation(
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @QueryMap params: Map<String, String>
    ): Response<Weather?>

    @GET(FORECAST_ST)
    suspend fun getForecastForLocation(
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @QueryMap params: Map<String, String>
    ): Response<ForecastResponse>
}