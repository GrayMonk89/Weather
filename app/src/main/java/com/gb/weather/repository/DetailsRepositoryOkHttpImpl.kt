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
        requestBuilder.url("$YANDEX_DOMAIN_PART${YANDEX_ENDPOINT}lat=${city.lat}&lon=${city.lon}")//url("{$YANDEX_DOMAIN_PART}{$YANDEX_ENDPOINT}{$LAT_KEY}={$city.lat}&{$LON_KEY}={$city.lon}")
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
            }
        }.start()
        //call.enqueue(callback)
        /*val callback: Callback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //mainView.let { Snackbar.make(it, "все плохо! $e", Snackbar.LENGTH_LONG).show() }
                mainView.showSnackBar("все еще плохее! $e", "", {}, Snackbar.LENGTH_LONG)
                binding.loadingLayout.visibility = View.GONE
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {

                    //val serverResponse:String =

                    val weatherDTO: WeatherDTO = Gson().fromJson(response.body()!!.string(),
                        WeatherDTO::class.java)
                    requireActivity().runOnUiThread{
                        renderData(weatherDTO)
                    }
                } else{
                    requireActivity().runOnUiThread{
                        binding.loadingLayout.visibility = View.GONE
                    }

                }
            }
        }*/
        //client.newCall(requestBuilder.build()).enqueue(callback)
    }
}