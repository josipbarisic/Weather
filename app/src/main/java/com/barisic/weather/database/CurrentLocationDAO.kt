package com.barisic.weather.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.barisic.weather.models.CurrentLocation
import com.barisic.weather.util.CURRENT_DATA_ID

@Dao
interface CurrentLocationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCurrentLocation(location: CurrentLocation)

    @Query("SELECT * FROM current_location WHERE id=$CURRENT_DATA_ID")
    fun getCurrentLocation(): LiveData<CurrentLocation>
}