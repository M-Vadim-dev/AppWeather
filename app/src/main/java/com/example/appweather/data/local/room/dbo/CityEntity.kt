package com.example.appweather.data.local.room.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val name: String,
    val isDefault: Boolean = false,
)