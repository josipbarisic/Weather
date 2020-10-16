package com.barisic.weather.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.barisic.weather.R
import com.barisic.weather.activities.MainActivity
import com.barisic.weather.activities.YouTubeActivity
import com.barisic.weather.adapters.ForecastRecyclerViewAdapter
import com.barisic.weather.databinding.FragmentMainBinding
import com.barisic.weather.models.CurrentLocation
import com.barisic.weather.models.Weather
import com.barisic.weather.models.ytsearch.YouTubeSearch
import com.barisic.weather.util.*
import com.barisic.weather.viewmodels.MainViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.recent_news_layout.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var mainActivity: MainActivity

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val locationHelper: LocationHelper by inject()

    private var locationManager: LocationManager? = null
    private val locationProviderIntentFilter =
        IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)

    private lateinit var forecastRecyclerViewAdapter: ForecastRecyclerViewAdapter

    private lateinit var fields: List<Place.Field>
    private lateinit var autoCompleteIntent: Intent

    private val searchObserver = Observer<Boolean> {
        Timber.d(it.toString())
        if (it) {
            requireActivity().startActivityForResult(autoCompleteIntent, AUTOCOMPLETE_REQUEST_CODE)
            mainViewModel.searchActive.value = false
        }
    }

    private val updatedWeatherObserver = Observer<Weather?> {
        it?.let {
            mainViewModel.youTubeSearchQuery.value = it.getYoutubeQueryString()
            mainViewModel.updateStoredCurrentWeather(it)
            Timber.d("updating weather ...")
        }
    }

    private val locationObserver = Observer<CurrentLocation?> {
        if (it != null && requireContext().isDeviceConnected()) {
            Timber.tag("LOCATION_FROM_DB").d("${it.lat} ${it.lng}")
            val latLng = LatLng(it.lat, it.lng)
            mainViewModel.coords.value = latLng
        }
    }

    private val storedWeatherObserver = Observer<Weather> {
        when {
            it != null -> setUpWeather(it)
            requireContext().isDeviceConnected() -> mainViewModel.refresh()
            else -> {
                mainViewModel.progressBarShowing.value = false
                mainViewModel.placeHolderShowing.value = true
            }
        }
    }

    private val forecastObserver = Observer<ArrayList<Weather>?> {
        it?.let {
            forecastRecyclerViewAdapter = ForecastRecyclerViewAdapter(it)
            forecastRecyclerViewAdapter.notifyDataSetChanged().also {
                setUpForecastRV()
            }
            mainViewModel.showingForecast.value = true
            mainViewModel.showingYouTubeVideo.value = true
        }
    }

    private val youTubeSearchResultObserver = Observer<YouTubeSearch> {
        it?.let {
            Picasso.get().load(it.getFirstVideoThumbnail()).into(youtube_holder.youtube_thumbnail)
        }
    }

    private val playVideoObserver = Observer<Boolean> {
        if (it) {
            mainViewModel.youTubeSearchResult.value?.getFirstVideoId()?.let { video ->
                openYouTubeVideo(video)
            }
            mainViewModel.playVideo.value = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fields = listOf(Place.Field.NAME, Place.Field.LAT_LNG)
        autoCompleteIntent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .setTypeFilter(TypeFilter.CITIES).build(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel
        mainViewModel.refresh()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.searchActive.observe(viewLifecycleOwner, searchObserver)
        mainViewModel.updatedWeather.observe(viewLifecycleOwner, updatedWeatherObserver)
        mainViewModel.currentLocation.observe(viewLifecycleOwner, locationObserver)
        mainViewModel.storedWeather.observe(viewLifecycleOwner, storedWeatherObserver)
        mainViewModel.forecast.observe(viewLifecycleOwner, forecastObserver)
        mainViewModel.youTubeSearchResult.observe(viewLifecycleOwner, youTubeSearchResultObserver)
        mainViewModel.playVideo.observe(viewLifecycleOwner, playVideoObserver)

        checkPermissionWithAction(Manifest.permission.ACCESS_FINE_LOCATION) {
            if (savedInstanceState == null) checkLocationSettings()
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(
            mainActivity.locationSettingsChangeReceiver,
            locationProviderIntentFilter
        )
    }

    override fun onStop() {
        super.onStop()
        locationHelper.stopLocationUpdates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().unregisterReceiver(mainActivity.locationSettingsChangeReceiver)
    }

    private fun setUpForecastRV() {
        binding.forecastHolder.forecastRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.forecastHolder.forecastRv.adapter = forecastRecyclerViewAdapter
    }

    private fun setUpWeather(weather: Weather) {
        main_layout.setBackgroundResource(weather.getWeatherImgRes())
        mainViewModel.progressBarShowing.value = false
        mainViewModel.placeHolderShowing.value = false
        if (requireContext().isDeviceConnected()) Picasso.get()
            .load("$ICON_URL${weather.weather[0].icon}@4x.png").into(weather_icon)
        units_icon.setImageDrawable(Common.getUnitDrawable(requireContext()))
    }

    private fun checkLocationSettings() {
        if (locationManager != null
            && !locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            && !locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            requireContext().showAlertDialog(
                R.string.location_query,
                R.string.open_location_settings,
                {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                },
                R.string.cancel,
                R.string.app_name
            )
        } else {
            locationHelper.startLocationUpdates()
        }
    }

    private fun checkPermissionWithAction(permission: String, action: () -> Unit) {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                action()
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                nav_host_fragment.requireView().showSnackBar(
                    R.string.location_permission_not_enabled,
                    Snackbar.LENGTH_SHORT
                )
            }

            override fun onPermissionRationaleShouldBeShown(
                permission: PermissionRequest?,
                token: PermissionToken?
            ) {
                requireContext().showToast(
                    R.string.location_permission_not_enabled,
                    Toast.LENGTH_LONG
                )
            }
        }

        Dexter.withActivity(requireActivity())
            .withPermission(permission)
            .withListener(permissionListener)
            .check()
    }

    private fun openYouTubeVideo(videoId: String) {
        val i = Intent(requireContext(), YouTubeActivity::class.java)
        i.putExtra(VIDEO_ID, videoId)
        startActivity(i)
    }
}