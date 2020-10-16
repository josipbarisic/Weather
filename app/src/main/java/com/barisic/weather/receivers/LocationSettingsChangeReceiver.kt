package com.barisic.weather.receivers

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import com.barisic.weather.util.Common
import com.barisic.weather.util.LocationHelper
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject
import timber.log.Timber

class LocationSettingsChangeReceiver : BroadcastReceiver(), KoinComponent {
    private var isGpsEnabled: Boolean = false
    private var isNetworkEnabled: Boolean = false
    private val locationHelper: LocationHelper by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.action?.let { actionName ->
            if (actionName == LocationManager.PROVIDERS_CHANGED_ACTION && context != null) {
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                isNetworkEnabled =
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                Timber.d(this.javaClass.name)
                if ((isGpsEnabled || isNetworkEnabled) &&
                    Common.checkPermission(get(), Manifest.permission.ACCESS_FINE_LOCATION)
                ) {
                    locationHelper.startLocationUpdates()
                }
            }
        }
    }
}