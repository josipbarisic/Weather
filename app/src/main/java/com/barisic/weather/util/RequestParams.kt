package com.barisic.weather.util

class RequestParams {
    companion object {
        private const val OWM_API_KEY = "e48dce4d0d75686d2125f263adc02de4"
        var count: Int = CNT_CURRENT
        var units: String = METRIC_UNITS
        fun getRequestParams(): Map<String, String> {
            val paramsMap = HashMap<String, String>()
            paramsMap["appid"] = OWM_API_KEY
            paramsMap["cnt"] = count.toString()
            paramsMap["units"] = units
            return paramsMap
        }
    }
}