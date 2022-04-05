package com.gb.weather.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.weather.repository.localpart.LocalLocalRepositoryImpl
import com.gb.weather.repository.serverpart.ServerRepositoryImpl

class MainViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val localRepository: LocalLocalRepositoryImpl = LocalLocalRepositoryImpl(),
    private val serverRepository: ServerRepositoryImpl = ServerRepositoryImpl()
) :
    ViewModel() {
    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getWeatherNotFromHere() = getWeather(true)
    fun getWeatherFromHere() = getWeather(false)

    private fun getWeather(notFromHere: Boolean) {
        Thread {
            liveData.postValue(AppState.Loading(0))

            if ((0..10).random() > 0) {
                val answer =
                    if (notFromHere)
                        localRepository.getWorldWeatherFromLocalStorage()
                    else
                        localRepository.getRussianWeatherFromLocalStorage()
                liveData.postValue(AppState.Success(answer))
            }
            else
                liveData.postValue(AppState.Error(IllegalAccessException("Все плохо")))
        }.start()
    }
}