package com.gb.weather.view.main


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.weather.repository.localpart.LocalLocalRepositoryImpl
import com.gb.weather.repository.serverpart.ServerRepositoryImpl
import com.gb.weather.viewmodel.AppState

class MainViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData(),
                    private val localRepository: LocalLocalRepositoryImpl = LocalLocalRepositoryImpl(),
                    private val serverRepository: ServerRepositoryImpl = ServerRepositoryImpl()
) :
    ViewModel() {
    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getWeather() {
        Thread {
            liveData.postValue(AppState.Loading(0))

            if ((0..10).random() > 5)
                liveData.postValue(AppState.Success(serverRepository.getWeatherFromServer()))
            else
                liveData.postValue(AppState.Error(IllegalAccessException("Все плохо")))
        }.start()
    }
}