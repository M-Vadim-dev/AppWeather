package com.example.appweather.domain.model


data class WeatherInfo(
    val cityId: Int,
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
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
    val pressure: Double,
    val cloud: Int,
    val sunrise: String,
    val sunset: String,
    val moonPhase: String,
    val moonIllumination: String,
    val forecast: List<ForecastItem>,
    val lastUpdatedEpoch: Long,
)
