package com.barisic.weather.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.barisic.weather.models.CurrentLocation
import com.barisic.weather.repositories.LocationRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import java.util.*

class LocationHelper(ctx: Context, locationRepository: LocationRepository) {
    companion object {
        private const val UPDATE_INTERVAL_MS: Long = 1000 * 60
        private const val FASTEST_UPDATE_INTERVAL_MS: Long = UPDATE_INTERVAL_MS / 2
    }

    private var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ctx)
    private val locationRequest = LocationRequest.create()
    private val sharedPrefs = SharedPrefs(ctx)

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult != null && locationResult.lastLocation != null) {
                val location = locationResult.lastLocation
                Timber.tag("LOCATION_RES").d(location.toString())
                if (Common.updateDue(sharedPrefs.getValueLong(UPDATE_TIME))) {
                    locationRepository.updateCurrentLocation(
                        CurrentLocation(
                            location.latitude,
                            location.longitude
                        )
                    )
                    sharedPrefs.save(UPDATE_TIME, Calendar.getInstance().getTimeInSeconds())
                }

            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        setupLocationRequest()
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
        Timber.d("LOCATION_UPDATED_START")
    }

    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun setupLocationRequest() {
        locationRequest.interval = UPDATE_INTERVAL_MS
        locationRequest.smallestDisplacement = 100f
        locationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_MS
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
}