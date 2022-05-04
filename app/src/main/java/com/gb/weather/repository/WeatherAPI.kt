package com.gb.weather.repository

import com.gb.weather.repository.dto.WeatherDTO
import com.gb.weather.utils.LAT_KEY
import com.gb.weather.utils.LON_KEY
import com.gb.weather.utils.YANDEX_API_KEY
import com.gb.weather.utils.YANDEX_ENDPOINT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherAPI {
    @GET(YANDEX_ENDPOINT)
    fun getWeather(
        @Header(YANDEX_API_KEY) apiKey: String,
        @Query(LAT_KEY) lat: Double,
        @Query(LON_KEY) lon: Double
    ): Call<WeatherDTO>
}