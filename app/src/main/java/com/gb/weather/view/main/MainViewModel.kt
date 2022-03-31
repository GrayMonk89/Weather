package com.gb.weather.view.main


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(private val liveData: MutableLiveData<Any> = MutableLiveData()) : ViewModel() {
    fun getData() : LiveData<Any>{
        return liveData
    }
}