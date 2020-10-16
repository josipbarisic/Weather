package com.barisic.weather.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.barisic.weather.R
import com.barisic.weather.receivers.LocationSettingsChangeReceiver
import com.barisic.weather.util.AUTOCOMPLETE_REQUEST_CODE
import com.barisic.weather.util.GoogleApiConfig
import com.barisic.weather.util.showToast
import com.barisic.weather.viewmodels.MainViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.widget.Autocomplete
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModel()

    val locationSettingsChangeReceiver = LocationSettingsChangeReceiver()
    private lateinit var navController: NavController
    private var doubleClickedBackToExit = false

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Places.initialize(this, GoogleApiConfig.getApiKey())
        navController = findNavController(R.id.nav_host_fragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE
            && resultCode == RESULT_OK
            && data != null
        ) {
            val place = Autocomplete.getPlaceFromIntent(data)
            Timber.tag("PLACE_RESULT").d("${place.name} ${place.latLng.toString()}")
            mainViewModel.coords.value = place.latLng
            mainViewModel.showingLocationBtn.value = true
        } else Timber.tag("PLACE_ERROR -> $resultCode, $requestCode NNN").d(data.toString())
    }

    override fun onBackPressed() {
        if (doubleClickedBackToExit) {
            finish()
        } else {
            doubleClickedBackToExit = true
            showToast(R.string.back_to_exit, Toast.LENGTH_SHORT)
            Handler().postDelayed({ doubleClickedBackToExit = false }, 2000)
        }
    }
}