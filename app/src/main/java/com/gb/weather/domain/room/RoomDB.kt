package com.gb.weather.domain.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(HistoryEntity::class), version = 1)
abstract class RoomDB: RoomDatabase() {
    abstract fun historyDAO(): HistoryDAO
}