package com.gb.weather.repository.detailse

import com.gb.weather.viewmodel.HistoryViewModel

interface DetailsRepositoryForAll {
    fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll)
}