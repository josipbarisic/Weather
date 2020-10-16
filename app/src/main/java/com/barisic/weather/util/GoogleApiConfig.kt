package com.barisic.weather.util

object GoogleApiConfig {
    private const val PLACES_API_KEY = "AIzaSyDZ0s2nrcISmy7tyulzIsXR71VmyHH2s4I"
    fun getApiKey(): String {
        return PLACES_API_KEY
    }
}