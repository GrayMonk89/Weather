package com.gb.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.weather.repository.City
import com.gb.weather.repository.DetailsRepository
import com.gb.weather.repository.DetailsRepositoryRetrofit2Impl
import com.gb.weather.repository.Weather


class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repository: DetailsRepository = DetailsRepositoryRetrofit2Impl(),/*DetailsRepositoryOkHttpImpl*/
) : ViewModel() {

    fun getLiveData() = liveData
    fun getWeather(city: City){
        repository.getWeatherDetails(city,object : Callback {
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
            }

            override fun onFailure(errorMessage: String) {
                liveData.postValue(DetailsState.Error(Throwable(errorMessage)))
            }
        })
    }

    interface Callback{
        fun onResponse(weather: Weather)
        fun onFailure(errorMessage: String)
    }
}