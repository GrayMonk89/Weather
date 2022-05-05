package com.gb.weather.domain.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_response_table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city: String,/*TODO составной первичный ключ timestamp + city*/
    var temperature: Int,
    val feelsLike: Int,
    val icon: String
) {
}