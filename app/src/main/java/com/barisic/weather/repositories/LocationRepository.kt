package com.barisic.weather.repositories

import androidx.lifecycle.LiveData
import com.barisic.weather.database.CurrentLocationDAO
import com.barisic.weather.models.CurrentLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class LocationRepository(private val currentLocationDAO: CurrentLocationDAO) {
    fun updateCurrentLocation(location: CurrentLocation) {
        GlobalScope.launch(Dispatchers.IO) {
            currentLocationDAO.updateCurrentLocation(location)
            Timber.d("${location.lat} ${location.lng}")
        }
    }

    fun getCurrentLocation(): LiveData<CurrentLocation> {
        return currentLocationDAO.getCurrentLocation()
    }
}