package com.barisic.weather.modules

import com.barisic.weather.util.LocationHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val helpersModule = module {
    single { LocationHelper(androidContext(), get()) }
}