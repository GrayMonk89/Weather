package com.gb.weather

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.gb.weather.domain.room.HistoryDAO
import com.gb.weather.domain.room.RoomDB
import com.gb.weather.repository.weather.WeatherAPI
import com.gb.weather.utils.LOG_KEY
import com.gb.weather.utils.YANDEX_DOMAIN_HARD_MODE_PART
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        private var dataBase: RoomDB? = null
        private var appContext: MyApp? = null
        fun getHistoryDAO(): HistoryDAO {
            if (dataBase == null) {
                if (appContext != null) {
                    dataBase = Room
                        .databaseBuilder(appContext!!, RoomDB::class.java, "history_response_table")
                        .build()
                } else {
                    throw IllegalStateException("Terrible, terrible things! appContext is NULL ")
                }
            }
            return dataBase!!.historyDAO()
        }

        fun getWeatherAPI(): WeatherAPI {
            val weatherAPI = Retrofit.Builder().apply {
                baseUrl(YANDEX_DOMAIN_HARD_MODE_PART)
                addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().setLenient().create()
                    )
                )
            }.build().create(WeatherAPI::class.java)
            return weatherAPI
        }
    }
}