package com.barisic.weather.network

import com.barisic.weather.models.ytsearch.YouTubeSearch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface YouTubeSearchService {
    @GET("search?")
    suspend fun getSearchResults(
        @Query("q") query: String,
        @QueryMap params: Map<String, String>
    ): Response<YouTubeSearch>
}