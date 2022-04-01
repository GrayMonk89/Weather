package com.gb.weather.repository.localpart

import com.gb.weather.repository.Weather

interface LocalRepository {
    fun getWorldWeatherFromLocalStorage():List<Weather>
    fun getRussianWeatherFromLocalStorage():List<Weather>
}