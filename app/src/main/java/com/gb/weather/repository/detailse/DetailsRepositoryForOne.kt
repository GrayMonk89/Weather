package com.gb.weather.repository.detailse

import com.gb.weather.repository.weather.City
import com.gb.weather.viewmodel.DetailsViewModel

interface DetailsRepositoryForOne {
    fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback)
}