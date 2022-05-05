package com.gb.weather.viewmodel


sealed class ResponseState {
    data class ErrorOnServerSide(val errorMessage: String) : ResponseState()
    data class ErrorOnClientSide(val errorMessage: String) : ResponseState()
    data class ErrorInJSONConversion(val errorMessage: String) : ResponseState()

}
