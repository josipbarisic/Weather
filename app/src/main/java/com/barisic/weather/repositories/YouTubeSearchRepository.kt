package com.barisic.weather.repositories

import androidx.lifecycle.LiveData
import com.barisic.weather.models.ytsearch.YouTubeSearch
import com.barisic.weather.network.YouTubeSearchService
import com.barisic.weather.util.YoutubeSearchRequestParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class YouTubeSearchRepository(private val youTubeSearchService: YouTubeSearchService) {
    fun search(query: String): LiveData<YouTubeSearch> {
        return object : LiveData<YouTubeSearch>() {
            override fun onActive() {
                super.onActive()
                GlobalScope.launch(Dispatchers.IO) {
                    val results = youTubeSearchService.getSearchResults(
                        query,
                        YoutubeSearchRequestParams.getParams()
                    )
                    withContext(Dispatchers.Main) {
                        value = results.body()
                    }
                }
            }
        }

    }
}