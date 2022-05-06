package com.gb.weather.repository.detailse

import com.gb.weather.repository.weather.Weather

interface DetailsRepositoryAdd {
    fun addWeather(weather: Weather)
}