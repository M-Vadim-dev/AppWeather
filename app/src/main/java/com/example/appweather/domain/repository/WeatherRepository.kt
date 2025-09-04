package com.example.appweather.domain.repository

import com.example.appweather.domain.model.WeatherInfo

interface WeatherRepository {
    suspend fun getWeather(cityName: String?, lat: Double?, lon: Double?): WeatherInfo
    suspend fun getWeatherWithFallback(cityName: String?, lat: Double?, lon: Double?): WeatherInfo
}
