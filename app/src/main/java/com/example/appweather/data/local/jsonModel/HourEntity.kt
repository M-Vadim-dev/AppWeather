package com.example.appweather.data.local.jsonModel

import kotlinx.serialization.Serializable

@Serializable
data class HourEntity(
    val time: String,
    val timeEpoch: Long,
    val tempC: Double,
    val tempF: Double,
    val isDay: Boolean,
    val condition: String,
    val iconUrl: String,
    val windKph: Double,
    val windMph: Double,
    val windDir: String,
    val humidity: Int,
    val cloud: Int,
    val feelsLikeC: Double,
    val feelsLikeF: Double,
    val windChillC: Double,
    val heatIndexC: Double,
    val dewPointC: Double,
    val willItRain: Boolean,
    val chanceOfRain: Int,
    val willItSnow: Boolean,
    val chanceOfSnow: Int,
    val visKm: Double,
    val uv: Double,
)
