package com.gb.weather.viewmodel

import com.gb.weather.repository.weather.Weather

sealed class DetailsState {
    object Loading:DetailsState()
    data class Success(val weather: Weather):DetailsState()
    data class Error(val error:Throwable):DetailsState()
}
