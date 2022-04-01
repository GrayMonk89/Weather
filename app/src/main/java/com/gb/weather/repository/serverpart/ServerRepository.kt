package com.gb.weather.repository.serverpart

import com.gb.weather.repository.Weather

interface ServerRepository {
    fun getWeatherFromServer(): Weather
}