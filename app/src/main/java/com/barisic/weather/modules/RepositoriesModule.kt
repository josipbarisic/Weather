package com.barisic.weather.modules

import com.barisic.weather.repositories.LocationRepository
import com.barisic.weather.repositories.WeatherRepository
import com.barisic.weather.repositories.YouTubeSearchRepository
import org.koin.dsl.module

val reposModule = module {
    single { WeatherRepository(get(), get()) }
    single { LocationRepository(get()) }
    single { YouTubeSearchRepository(get()) }
}