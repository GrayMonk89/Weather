package com.gb.weather.repository.localpart

import com.gb.weather.repository.Weather
import com.gb.weather.repository.getRussianCities
import com.gb.weather.repository.getWorldCities

class LocalLocalRepositoryImpl: LocalRepository {
    override fun getWorldWeatherFromLocalStorage():List<Weather> {
        return getWorldCities()
    }
    override fun getRussianWeatherFromLocalStorage():List<Weather> {
        return getRussianCities()
    }
}