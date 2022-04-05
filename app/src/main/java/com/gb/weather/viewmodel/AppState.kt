package com.gb.weather.viewmodel

import com.gb.weather.repository.Weather

sealed class AppState {
    data class Loading(private val process: Int) : AppState()
    data class Success(private val weatherListData: List<Weather>) : AppState(){
        fun getWeatherListData() = weatherListData
    }
    data class Error(private val error: Throwable) : AppState(){
        fun getError() = error
    }
}
