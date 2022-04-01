package com.gb.weather.repository.serverpart

import com.gb.weather.repository.Weather

class ServerRepositoryImpl: ServerRepository {
    override fun getWeatherFromServer(): Weather {
        Thread.sleep(1500L)
        return Weather()
    }
}