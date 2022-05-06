package com.gb.weather.repository.detailse

import com.gb.weather.MyApp
import com.gb.weather.repository.weather.City
import com.gb.weather.repository.weather.Weather
import com.gb.weather.utils.convertHistoryEntityToWeather
import com.gb.weather.utils.convertWeatherToEntity
import com.gb.weather.viewmodel.DetailsViewModel
import com.gb.weather.viewmodel.HistoryViewModel

class DetailsRepositoryRoomImpl : DetailsRepositoryForOne, DetailsRepositoryForAll,
    DetailsRepositoryAdd {
    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {
        Thread{val weatherList = convertHistoryEntityToWeather(MyApp.getHistoryDAO().getHistoryForCity(city.cityName))
            if (weatherList.isEmpty()) {
                callback.onFailure("У города ${city.cityName} нет истории запросов")
            } else {
                callback.onResponse(weatherList.last())
            }}.start()
    }

    override fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll) {
        Thread {
            callback.onResponse(convertHistoryEntityToWeather(MyApp.getHistoryDAO().getAll()))
        }.start()
    }

    override fun addWeather(weather: Weather) {
        MyApp.getHistoryDAO().insert(convertWeatherToEntity(weather))
    }

}