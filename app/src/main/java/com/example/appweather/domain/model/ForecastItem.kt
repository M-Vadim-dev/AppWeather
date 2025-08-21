package com.example.appweather.domain.model

data class ForecastItem(
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
    val astro: AstroInfo,
    val hours: List<HourInfo>,
)
