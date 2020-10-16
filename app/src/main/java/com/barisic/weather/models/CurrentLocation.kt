package com.barisic.weather.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.barisic.weather.util.CURRENT_DATA_ID
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "current_location")
data class CurrentLocation(
    var lat: Double,
    var lng: Double,
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_DATA_ID
) {
    @Ignore
    fun getLatLng(): LatLng {
        return LatLng(lat, lng)
    }
}