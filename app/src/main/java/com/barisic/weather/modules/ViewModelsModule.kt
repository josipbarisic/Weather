package com.barisic.weather.modules

import com.barisic.weather.viewmodels.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { MainViewModel(androidContext(), get(), get(), get()) }
}