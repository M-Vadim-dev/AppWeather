package com.example.appweather.data.local.room.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val lat: Double,
    val lon: Double,
    val isDefault: Boolean = false,
)