package com.gb.weather.repository.serverpart

import com.gb.weather.repository.Weather
import com.gb.weather.utils.SERVER_REPOSITORY_IMPL_SLEEP_TIME

class ServerRepositoryImpl: ServerRepository {
    override fun getWeatherFromServer(): Weather {
        Thread.sleep(SERVER_REPOSITORY_IMPL_SLEEP_TIME)
        return Weather()
    }
}