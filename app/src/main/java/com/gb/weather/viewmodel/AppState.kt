package com.gb.weather.viewmodel

import com.gb.weather.repository.Weather

sealed class AppState {

    data class Loading(val process: Int) : AppState()
    data class Success(val weatherData: Weather) : AppState()
    data class Error(val error: Throwable) : AppState()
}
