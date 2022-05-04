package com.gb.weather.repository

import com.gb.weather.BuildConfig
import com.gb.weather.repository.dto.WeatherDTO
import com.gb.weather.utils.*
import com.gb.weather.viewmodel.DetailsViewModel
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request


class DetailsRepositoryOkHttpImpl:DetailsRepository {
    override fun getWeatherDetails(city: City,callback: DetailsViewModel.Callback) {
        val client = OkHttpClient()
        val requestBuilder = Request.Builder()
        requestBuilder.addHeader(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
        requestBuilder.url("$YANDEX_DOMAIN_HARD_MODE_PART${YANDEX_ENDPOINT}${LAT_KEY}=${city.lat}&${LON_KEY}=${city.lon}")//url("{$YANDEX_DOMAIN_PART}{$YANDEX_ENDPOINT}{$LAT_KEY}={$city.lat}&{$LON_KEY}={$city.lon}")
        val request = requestBuilder.build()
        val call = client.newCall(request)
        Thread{
            val response = call.execute()
            if(response.isSuccessful){
                val serverResponse = response.body()!!.string()
                val weatherDTO: WeatherDTO = Gson().fromJson(serverResponse,WeatherDTO::class.java)
                val weather = convertDtoToModel(weatherDTO)
                weather.city = city
                callback.onResponse(weather)
            } else {
                callback.onFailure("Ответ прише не верный. ${response.body().toString()}")//errorBody().toString()
            }
        }.start()
    }
}