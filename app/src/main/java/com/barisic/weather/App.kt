package com.barisic.weather

import android.app.Application
import com.barisic.weather.modules.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@App)
            loadKoinModules(
                listOf(
                    viewModelsModule,
                    reposModule,
                    networkModule,
                    helpersModule,
                    databaseModule
                )
            )
        }
    }
}