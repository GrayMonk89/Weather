package com.gb.weather.repository

data class Weather(val city: City = getDefaultCity(),var temperature:Int = 0,val feelsLike:Int = 0)

fun getDefaultCity() = City("Москва", 55.75, 37.61)

data class City(val cityName: String, val lat: Double, val lon: Double)
