package com.barisic.weather.modules

import com.barisic.weather.network.WeatherService
import com.barisic.weather.network.YouTubeSearchService
import com.barisic.weather.util.BASE_API_URL
import com.barisic.weather.util.GOOGLE_RETROFIT
import com.barisic.weather.util.OWM_RETROFIT
import com.barisic.weather.util.YOUTUBE_SEARCH_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        val builder = OkHttpClient.Builder()
        builder.callTimeout(10, TimeUnit.SECONDS)
        builder.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()
    }

    single(qualifier = named(OWM_RETROFIT)) {
        Retrofit.Builder()
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_API_URL)
            .build()
    }

    single(qualifier = named(GOOGLE_RETROFIT)) {
        Retrofit.Builder()
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(YOUTUBE_SEARCH_URL)
            .build()
    }

    single { get<Retrofit>(named(OWM_RETROFIT)).create(WeatherService::class.java) }
    single { get<Retrofit>(named(GOOGLE_RETROFIT)).create(YouTubeSearchService::class.java) }
}