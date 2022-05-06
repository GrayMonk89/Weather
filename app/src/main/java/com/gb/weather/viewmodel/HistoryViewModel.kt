package com.gb.weather.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.weather.repository.detailse.DetailsRepositoryRoomImpl
import com.gb.weather.repository.weather.Weather

class HistoryViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: DetailsRepositoryRoomImpl = DetailsRepositoryRoomImpl(),
) :
    ViewModel() {
    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getAll(){
        repository.getAllWeatherDetails(object :CallbackForAll{
            override fun onResponse(listWeather: List<Weather>) {
                liveData.postValue(AppState.Success(listWeather))
            }

            override fun onFailure(errorMessage: String) {
                TODO("Not yet implemented")
            }

        })
    }

    interface CallbackForAll{
        fun onResponse(listWeather: List<Weather>)
        fun onFailure(errorMessage: String)
    }
}