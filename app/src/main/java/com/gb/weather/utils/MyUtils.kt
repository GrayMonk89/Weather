package com.gb.weather.utils

import android.view.View
import com.gb.weather.repository.weather.Weather
import com.gb.weather.repository.dto.FactDTO
import com.gb.weather.repository.dto.WeatherDTO
import com.gb.weather.repository.weather.getDefaultCity
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}


fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: FactDTO = weatherDTO.factDTO
    return (Weather(getDefaultCity(), fact.temperature, fact.feelsLike, fact.icon))
}

class MyUtils {

}
