package com.example.appweather.ui.screens.weather

import androidx.compose.runtime.Immutable
import java.time.LocalDateTime

@Immutable
data class WeatherUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isNetworkAvailable: Boolean = true,
    val cityName: String = "",
    val dateText: String = "",
    val timeToday: LocalDateTime = LocalDateTime.now(),
    val weekDayText: String = "",
    val country: String = "",
    val temperature: Int = 0,
    val condition: String = "",
    val iconUrl: String = "",
    val isDay: Boolean = true,
    val humidity: Int = 0,
    val windSpeed: Double = 0.0,
    val windDir: String = "",
    val uvIndex: Double = 0.0,
    val pressure: Double = 0.0,
    val cloud: Int = 0,
    val forecastDays: List<ForecastUiItem> = emptyList(),
    val hourlyForecast: List<HourlyForecastUiItem> = emptyList(),
    val astroInfo: AstroUiItem? = null,
)

@Immutable
data class ForecastUiItem(
    val date: String = "",
    val iconUrl: String = "",
    val condition: String = "",
    val minTemp: Int = 0,
    val maxTemp: Int = 0,
    val avgTemp: Int = 0,
)

@Immutable
data class HourlyForecastUiItem(
    val time: String = "",
    val temperature: Int = 0,
    val iconUrl: String = "",
    val condition: String = "",
    val humidity: Int = 0,
    val windSpeed: Double = 0.0,
    val windDir: String = "",
)

@Immutable
data class AstroUiItem(
    val sunrise: String = "",
    val sunset: String = "",
    val moonrise: String = "",
    val moonset: String = "",
    val moonPhase: String = "",
    val moonIllumination: String = "",
)