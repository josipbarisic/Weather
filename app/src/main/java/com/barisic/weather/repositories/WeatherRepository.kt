package com.barisic.weather.repositories

import androidx.lifecycle.LiveData
import com.barisic.weather.database.CurrentWeatherDAO
import com.barisic.weather.models.Weather
import com.barisic.weather.network.WeatherService
import com.barisic.weather.util.CNT_CURRENT
import com.barisic.weather.util.CNT_FORECAST
import com.barisic.weather.util.RequestParams
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class WeatherRepository(
    private val weatherService: WeatherService,
    private val currentWeatherDAO: CurrentWeatherDAO
) {
    fun updateStoredCurrentWeather(weather: Weather) {
        GlobalScope.launch(Dispatchers.IO) {
            Timber.d(Gson().toJson(weather))
            Timber.d(currentWeatherDAO.updateCurrentWeather(weather).toString())
        }
    }

    fun getStoredCurrentWeatherData(): LiveData<Weather> {
        return currentWeatherDAO.getCurrentWeather()
    }

    fun getWeatherByLocation(coords: LatLng): LiveData<Weather?> {
        RequestParams.count = CNT_CURRENT
        return object : LiveData<Weather?>() {
            override fun onActive() {
                super.onActive()
                GlobalScope.launch(Dispatchers.IO) {
                    val weather =
                        weatherService.getCurrentWeatherForLocation(
                            coords.latitude.toString(),
                            coords.longitude.toString(),
                            RequestParams.getRequestParams()
                        )
                    withContext(Dispatchers.Main) {
                        value = weather.body()
                    }

                }
            }
        }
    }

    fun getForecastByLocation(coords: LatLng): LiveData<ArrayList<Weather>?> {
        RequestParams.count = CNT_FORECAST
        return object : LiveData<ArrayList<Weather>?>() {
            override fun onActive() {
                super.onActive()
                GlobalScope.launch(Dispatchers.IO) {
                    val weather =
                        weatherService.getForecastForLocation(
                            coords.latitude.toString(),
                            coords.longitude.toString(),
                            RequestParams.getRequestParams()
                        )
                    withContext(Dispatchers.Main) {
                        value = weather.body()?.forecast
                    }
                }
            }
        }
    }
}