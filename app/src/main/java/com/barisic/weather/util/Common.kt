package com.barisic.weather.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.barisic.weather.R
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object Common {
    private val sdfDay = SimpleDateFormat("dd")
    private val sdfDate = SimpleDateFormat("dd/MM")
    private val sdfHours = SimpleDateFormat("HH:mm")

    fun checkPermission(ctx: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            ctx,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun updateDue(weatherTime: Long): Boolean {
        return Calendar.getInstance().getTimeInSeconds() > weatherTime + UPDATE_INTERVAL
    }

    fun getHoursFromUnix(unix: Long): String {
        val date = Date(unix * 1000)
        return sdfHours.format(date)
    }

    fun getDayFromUnix(unix: Long): String {
        val date = Date(unix * 1000)
        return sdfDay.format(date)
    }

    fun getDateFromUnix(unix: Long): String {
        val date = Date(unix * 1000)
        return sdfDate.format(date)
    }

    fun getUnitDrawable(ctx: Context): Drawable? {
        return when (RequestParams.units) {
            METRIC_UNITS -> ctx.getContextCompatDrawable(R.drawable.ic_celsius)
            IMPERIAL_UNITS -> ctx.getContextCompatDrawable(R.drawable.ic_fahrenheit)
            else -> ctx.getContextCompatDrawable(R.drawable.ic_kelvin)
        }
    }
}