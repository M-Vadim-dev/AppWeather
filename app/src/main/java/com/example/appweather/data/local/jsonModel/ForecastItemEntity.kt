package com.example.appweather.data.local.jsonModel

import kotlinx.serialization.Serializable

@Serializable
data class ForecastItemEntity(
    val date: String,
    val dateEpoch: Long,
    val minTempC: Double,
    val maxTempC: Double,
    val avgTempC: Double,
    val maxWindKph: Double,
    val maxWindMph: Double,
    val totalPrecipMm: Double,
    val totalSnowCm: Double,
    val avgHumidity: Double,
    val dailyWillItRain: Int,
    val dailyChanceOfRain: Int,
    val dailyWillItSnow: Int,
    val dailyChanceOfSnow: Int,
    val condition: String,
    val iconUrl: String,
    val astro: AstroEntity,
    val hours: List<HourEntity>,
)
