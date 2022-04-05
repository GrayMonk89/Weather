package com.gb.weather.view.weatherlist

import com.gb.weather.repository.Weather

interface OnItemListClickListener {
    fun onItemClick(weather: Weather)
}