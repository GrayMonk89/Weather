package com.gb.weather.utils

import com.gb.weather.domain.room.HistoryEntity
import com.gb.weather.repository.dto.FactDTO
import com.gb.weather.repository.dto.WeatherDTO
import com.gb.weather.repository.weather.City
import com.gb.weather.repository.weather.Weather
import com.gb.weather.repository.weather.getDefaultCity


fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: FactDTO = weatherDTO.factDTO
    return (Weather(getDefaultCity(), fact.temperature, fact.feelsLike, fact.icon))
}

fun convertWeatherToEntity(weather: Weather):HistoryEntity{
    return HistoryEntity(0,weather.city.cityName,weather.temperature,weather.feelsLike,weather.icon)
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>):List<Weather>{
    return entityList.map {
        Weather(City(it.city,0.0,0.0),it.temperature,it.feelsLike,it.icon)
    }
}

class MyUtils {

}
