package com.gb.weather.repository.detailse

import android.util.Log
import com.gb.weather.BuildConfig
import com.gb.weather.repository.weather.City
import com.gb.weather.repository.weather.WeatherAPI
import com.gb.weather.utils.LOG_KEY
import com.gb.weather.utils.YANDEX_DOMAIN_HARD_MODE_PART
import com.gb.weather.utils.convertDtoToModel
import com.gb.weather.viewmodel.DetailsViewModel

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailsRepositoryRetrofit2Impl : DetailsRepository {
    override fun getWeatherDetails(city: City, callbackDVM: DetailsViewModel.Callback) {
        val weatherAPI = Retrofit.Builder().apply {
            baseUrl(YANDEX_DOMAIN_HARD_MODE_PART)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build().create(WeatherAPI::class.java)

        Thread {
            try {
                val responseA =
                    weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY, city.lat, city.lon).execute()
                if (responseA.isSuccessful) {
                    responseA.body()?.let {
                        callbackDVM.onResponse(convertDtoToModel(it).apply {
                            this.city.cityName = city.cityName
                        })
                    }
                } else {
                    callbackDVM.onFailure("Ответ прише не верный. ${responseA.errorBody().toString()}")
                }
            } catch (e: JsonSyntaxException) {
                Log.d(LOG_KEY,"e.message ${e.message.toString()} ][ ${e.toString()}")
                callbackDVM.onFailure(e.message.toString())
            }
        }.start()

        /*weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY,city.lat,city.lon).enqueue(object :Callback<WeatherDTO>{
            override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                if(response.isSuccessful){
                    response.body()?.let {
                        callbackDVM.onResponse(convertDtoToModel(it))
                    }
                }
            }

            override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                callbackDVM.onFailure("Ответ прише не верный. ${responseA.errorBody().toString()}")
            }
        })*/
    }
}