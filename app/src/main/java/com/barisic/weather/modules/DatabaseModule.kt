package com.barisic.weather.modules

import androidx.room.Room
import com.barisic.weather.database.WeatherDatabase
import com.barisic.weather.util.DATABASE_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), WeatherDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration().build()
    }
    single { get<WeatherDatabase>().currentWeatherDAO }
    single { get<WeatherDatabase>().currentLocationDAO }
}