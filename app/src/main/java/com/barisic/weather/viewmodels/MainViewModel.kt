package com.barisic.weather.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.barisic.weather.models.Weather
import com.barisic.weather.models.ytsearch.YouTubeSearch
import com.barisic.weather.repositories.LocationRepository
import com.barisic.weather.repositories.WeatherRepository
import com.barisic.weather.repositories.YouTubeSearchRepository
import com.barisic.weather.util.LAT
import com.barisic.weather.util.LNG
import com.barisic.weather.util.isDeviceConnected
import com.google.android.gms.maps.model.LatLng

class MainViewModel(
    ctx: Context,
    private val weatherRepo: WeatherRepository,
    locationRepo: LocationRepository,
    private val youTubeSearchRepo: YouTubeSearchRepository
) : ViewModel() {
    val progressBarShowing = MutableLiveData(true)
    val placeHolderShowing = MutableLiveData(false)
    val showingLocationBtn = MutableLiveData(false)
    val showingForecast = MutableLiveData(false)
    val showingYouTubeVideo = MutableLiveData(false)
    val playVideo = MutableLiveData(false)
    val coords = MutableLiveData<LatLng>()
    val searchActive = MutableLiveData(false)
    val currentLocation = locationRepo.getCurrentLocation()
    var storedWeather: LiveData<Weather> = weatherRepo.getStoredCurrentWeatherData()
    var weatherHolder = MediatorLiveData<Weather>()
    val updatedWeather: LiveData<Weather?> =
        Transformations.switchMap(coords) { coords ->
            if (ctx.isDeviceConnected()) weatherRepo.getWeatherByLocation(coords) else null
        }
    val forecast = Transformations.switchMap(coords) { coords ->
        if (ctx.isDeviceConnected()) weatherRepo.getForecastByLocation(coords) else null
    }
    val youTubeSearchQuery = MutableLiveData<String>()
    val youTubeSearchResult: LiveData<YouTubeSearch> =
        Transformations.switchMap(youTubeSearchQuery) { query ->
            youTubeSearchRepo.search(query)
        }

    init {
        weatherHolder.addSource(storedWeather) {
            weatherHolder.value = it
        }
        weatherHolder.addSource(updatedWeather) {
            weatherHolder.value = it
        }
    }

    fun search() {
        searchActive.value = true
    }

    fun updateStoredCurrentWeather(weather: Weather) {
        weatherRepo.updateStoredCurrentWeather(weather)
    }

    fun refresh() {
        coords.value = if (currentLocation.value != null) currentLocation.value!!.getLatLng()
        else LatLng(LAT, LNG)
        showingLocationBtn.value = false
    }

    fun playVideo() {
        playVideo.value = true
    }
}