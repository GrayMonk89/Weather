package com.gb.weather.repository.serverpart

import com.gb.weather.repository.weather.Weather

interface ServerRepository {
    fun getWeatherFromServer(): Weather
}