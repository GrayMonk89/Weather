package com.gb.weather.repository

import com.gb.weather.viewmodel.ResponseState

fun interface OnServerResponseListener {
    fun onError(error: ResponseState)
}