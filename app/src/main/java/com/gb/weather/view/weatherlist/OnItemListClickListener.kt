package com.gb.weather.view.weatherlist

import com.gb.weather.repository.weather.Weather

interface OnItemListClickListener {
    fun onItemClick(weather: Weather)
}