package com.example.appweather.domain.repository

import com.example.appweather.domain.model.WeatherInfo

interface WeatherRepository {
    suspend fun getWeather(city: String): WeatherInfo
    suspend fun getWeather(lat: Double, lon: Double): WeatherInfo
    suspend fun saveDefaultCity(city: String)
    suspend fun getDefaultCity(): String?
    suspend fun getAllCities(): List<String>
    suspend fun addCity(city: String)
    suspend fun removeCity(city: String)

}
