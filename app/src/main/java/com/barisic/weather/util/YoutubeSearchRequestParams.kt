package com.barisic.weather.util

class YoutubeSearchRequestParams {
    companion object {
        var part: String = "snippet"
        var maxResults: String = "1"
        var type: String = "video"
        private const val API_KEY: String = "AIzaSyDZ0s2nrcISmy7tyulzIsXR71VmyHH2s4I"
        fun getParams(): Map<String, String> {
            val paramsMap = HashMap<String, String>()
            paramsMap["part"] = part
            paramsMap["maxResults"] = maxResults
            paramsMap["type"] = type
            paramsMap["key"] = API_KEY
            return paramsMap
        }
    }
}