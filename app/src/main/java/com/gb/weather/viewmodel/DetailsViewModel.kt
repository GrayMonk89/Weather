package com.gb.weather.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.weather.MyApp
import com.gb.weather.repository.detailse.DetailsRepositoryAdd
import com.gb.weather.repository.detailse.DetailsRepositoryForOne
import com.gb.weather.repository.detailse.DetailsRepositoryForOneRetrofit2Impl
import com.gb.weather.repository.detailse.DetailsRepositoryRoomImpl
import com.gb.weather.repository.weather.City
import com.gb.weather.repository.weather.Weather
import com.gb.weather.utils.DEFAULT_VALUE_BOOLEAN_FALSE


class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repositoryAdd: DetailsRepositoryAdd = DetailsRepositoryRoomImpl()
) : ViewModel() {

    private var repositoryForOne: DetailsRepositoryForOne = DetailsRepositoryForOneRetrofit2Impl()

    fun getLiveData() = liveData

    fun getWeather(city: City) {

        repositoryForOne = if (MyApp.isOnline()) {
            DetailsRepositoryForOneRetrofit2Impl()
        } else {
            DetailsRepositoryRoomImpl()
        }

        repositoryForOne.getWeatherDetails(city, object : Callback {
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
                repositoryAdd.addWeather(weather)
            }

            override fun onFailure(errorMessage: String) {
                liveData.postValue(DetailsState.Error(Throwable(errorMessage)))
            }
        })
    }

    private fun isOnline(): Boolean {
        return DEFAULT_VALUE_BOOLEAN_FALSE
    }

    interface Callback {
        fun onResponse(weather: Weather)
        fun onFailure(errorMessage: String)
    }
}