package com.gb.weather.viewmodel

sealed class AppState {

    data class Loading(val process: Int) : AppState()
    data class Success(val weatherData: Any) : AppState()
    data class Error(val error: Throwable) : AppState()
}
