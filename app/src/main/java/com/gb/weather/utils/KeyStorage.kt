package com.gb.weather.utils

//region Key
const val BUNDLE_WEATHER_KEY = "key bundle weather"
const val LOG_KEY = "(╯°□°)╯┻━━┻"
const val LOG_KEY_SECOND = "@@@"
const val MAIN_ACTIVITY_KEY = "MainActivity key"
const val MAIN_SERVICE_KEY = "MainService key"
const val BROADCAST_RECEIVER_CHANNEL_KEY = "Service broadcast channel"
const val BROADCAST_RECEIVER_CHANNEL_WEATHER_KEY = "Service broadcast weather channel"
const val SERVICE_BROADCAST_WEATHER_KEY = "weather service broadcast"
const val LAT_KEY = "lat"
const val LON_KEY = "lon"
//endregion

//region Yandex domain
const val YANDEX_API_KEY = "X-Yandex-API-Key"
const val YANDEX_DOMAIN_PART = "https://api.weather.yandex.ru/"//lat=$lat&lon=$lon"
const val YANDEX_DOMAIN_HARD_MODE_PART = "http://212.86.114.27/"
const val YANDEX_ENDPOINT = "v2/informers?"
//endregion

//region Constants
const val MAIN_SERVICE_HARD_WORK_TIME = 1000L
const val SERVER_REPOSITORY_IMPL_SLEEP_TIME = 1500L
const val MIL_SEC_TO_SEC_MULTIPLIER = 1000L
const val CONNECT_TIMEOUT = 1000
const val READ_TIMEOUT = 1000
//endregion

//region RestAPI
val SERVER_SIDE = 500..599
val CLIENT_SIDE = 400..499
val RESPONSEOK = 200..299
//endregion

class KeyStorage {
}