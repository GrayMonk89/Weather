package com.gb.weather

import android.app.Application
import androidx.room.Room
import com.gb.weather.domain.room.HistoryDAO
import com.gb.weather.domain.room.RoomDB

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
    }
}