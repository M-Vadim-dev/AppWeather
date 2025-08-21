package com.example.appweather.data.local.room.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appweather.data.local.jsonModel.ForecastItemEntity

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val city: String,
    val country: String,
    val localtime: String,
    val temperatureC: Double,
    val temperatureF: Double,
    val condition: String,
    val iconUrl: String,
    val windKph: Double,
    val windMph: Double,
    val windDir: String,
    val humidity: Int,
    val uvIndex: Double,
    val cloud: Int,
    val sunrise: String,
    val sunset: String,
    val moonPhase: String,
    val moonIllumination: String,
    val isDay: Boolean,
    val forecast: List<ForecastItemEntity>,
    val lastUpdatedEpoch: Long,
)