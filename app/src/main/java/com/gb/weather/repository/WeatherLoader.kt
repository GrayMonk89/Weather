package com.gb.weather.repository

import android.os.Handler
import android.os.Looper
import com.gb.weather.repository.dto.WeatherDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onServerResponseListener: OnServerResponse) {
    fun loadWeather(lat: Double, lon: Double) {
        val urlText = "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
        val uri = URL(urlText)
        val urlConnection: HttpsURLConnection = (uri.openConnection() as HttpsURLConnection).apply {
            connectTimeout = 1000
            readTimeout = 1000
            addRequestProperty("X-Yandex-API-Key","5f7a5661-e994-4c0e-89ee-6f09cdbf0574")
        }

        Thread {
            val headers = urlConnection.headerFields
            val responseCode = urlConnection.responseCode
            val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val weatherDTO: WeatherDTO = Gson().fromJson(
                buffer,
                WeatherDTO::class.java
            )
            Handler(Looper.getMainLooper()).post {
                onServerResponseListener.onResponse(weatherDTO)
            }
        }.start()

    }
}