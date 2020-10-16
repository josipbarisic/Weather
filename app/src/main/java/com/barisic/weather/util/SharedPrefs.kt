package com.barisic.weather.util

import android.content.Context
import net.grandcentrix.tray.AppPreferences
import timber.log.Timber

class SharedPrefs(private val context: Context) {

    private var instance: AppPreferences? = null
    private fun getPrefs(): AppPreferences {
        if (instance == null) {
            instance = AppPreferences(context)
        }
        return instance!!
    }

    fun save(KEY_NAME: String, value: Long) {
        Timber.tag(SHARED_PREFS).d("SAVE_INT: $value")
        getPrefs().put(KEY_NAME, value)
    }

    fun getValueLong(keyName: String): Long {
        Timber.tag(SHARED_PREFS).d("GET_INT: ${getPrefs().getLong(keyName, 0)}")
        return getPrefs().getLong(keyName, 0)
    }

    /*fun clearSharedPreference() {
        getPrefs().clear()
    }*/
}