package com.gb.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.weather.repository.detailse.DetailsRepositoryAdd
import com.gb.weather.repository.detailse.DetailsRepositoryForOne
import com.gb.weather.repository.detailse.DetailsRepositoryForOneRetrofit2Impl
import com.gb.weather.repository.detailse.DetailsRepositoryRoomImpl
import com.gb.weather.repository.weather.City
import com.gb.weather.repository.weather.Weather


class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repositoryForOne: DetailsRepositoryForOne = DetailsRepositoryForOneRetrofit2Impl(),
    private val repositoryAdd: DetailsRepositoryAdd = DetailsRepositoryRoomImpl()
) : ViewModel() {

    fun getLiveData() = liveData
    fun getWeather(city: City){
        repositoryForOne.getWeatherDetails(city,object : Callback {
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
                repositoryAdd.addWeather(weather)
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