package com.barisic.weather.util

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.barisic.weather.R
import com.google.android.material.snackbar.Snackbar
import java.util.*

fun View.showSnackBar(textRes: Int, length: Int) {
    Snackbar.make(this, textRes, length).show()
}

fun Calendar.getTimeInSeconds(): Long {
    return this.timeInMillis / 1000
}

@Suppress("DEPRECATION")
fun Context.isDeviceConnected(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        }
    } else {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (networkInfo != null) {
            return (networkInfo.isConnected &&
                    (networkInfo.type == ConnectivityManager.TYPE_WIFI ||
                            networkInfo.type == ConnectivityManager.TYPE_MOBILE ||
                            networkInfo.type == ConnectivityManager.TYPE_VPN))
        }
    }
    return false
}

fun Context.showToast(message: Int, length: Int) {
    Toast.makeText(this, message, length).show()
}

fun Context.showAlertDialog(
    message: Int,
    positiveButtonText: Int,
    positiveButtonFunction: () -> Unit,
    negativeButtonText: Int,
    title: Int
) {
    AlertDialog.Builder(this, R.style.WhiteDialogBackgroundStyle)
        .setMessage(message)
        .setPositiveButton(positiveButtonText) { _, _ ->
            positiveButtonFunction()
        }
        .setCancelable(false)
        .setNegativeButton(negativeButtonText, null)
        .setTitle(title)
        .show()
}

fun Context.getContextCompatDrawable(drawableRes: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableRes)
}