package com.gb.weather.repository.localpart

import com.gb.weather.repository.weather.Weather
import com.gb.weather.repository.weather.getRussianCities
import com.gb.weather.repository.weather.getWorldCities

class LocalLocalRepositoryImpl: LocalRepository {
    override fun getWorldWeatherFromLocalStorage():List<Weather> {
        return getWorldCities()
    }
    override fun getRussianWeatherFromLocalStorage():List<Weather> {
        return getRussianCities()
    }
}