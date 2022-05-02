package com.gb.weather.repository

import com.gb.weather.repository.dto.WeatherDTO
import com.gb.weather.viewmodel.DetailsViewModel

interface DetailsRepository {
    fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback)
}