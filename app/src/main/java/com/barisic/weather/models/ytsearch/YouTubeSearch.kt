package com.barisic.weather.models.ytsearch

data class YouTubeSearch(
    val items: List<Item>
) {
    fun getFirstVideoId(): String {
        return items[0].id.videoId
    }

    fun getFirstVideoThumbnail(): String {
        return items[0].snippet.thumbnails.high.url
    }
}