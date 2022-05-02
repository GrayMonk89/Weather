package com.gb.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.weather.repository.City
import com.gb.weather.repository.DetailsRepository
import com.gb.weather.repository.DetailsRepositoryOkHttpImpl
import com.gb.weather.repository.Weather

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repository: DetailsRepository = DetailsRepositoryOkHttpImpl(),
) : ViewModel() {

    fun getLiveData() = liveData
    fun getWeather(city: City){
        repository.getWeatherDetails(city,object : Callback {
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
            }

        })
    }

    interface Callback{
        fun onResponse(weather: Weather)
    }
}