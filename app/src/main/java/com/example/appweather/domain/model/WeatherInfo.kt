package com.example.appweather.domain.model


data class WeatherInfo(
    val city: String,
    val country: String,
    val localtime: String,
    val temperatureC: Double,
    val temperatureF: Double,
    val condition: String,
    val iconUrl: String,
    val isDay: Boolean,
    val humidity: Int,
    val windKph: Double,
    val windMph: Double,
    val windDir: String,
    val uvIndex: Double,
    val cloud: Int,
    val sunrise: String,
    val sunset: String,
    val moonPhase: String,
    val moonIllumination: String,
    val forecast: List<ForecastItem>,
    val lastUpdatedEpoch: Long,
)
