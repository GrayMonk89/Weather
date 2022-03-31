package com.gb.weather.repository

class RepositoryImpl:Repository {
    override fun getWeatherFromServer():Weather {
        Thread.sleep(1500L)
        return Weather()
    }

    override fun getWeatherFromLocalStorage():Weather {
        return Weather()
    }
}