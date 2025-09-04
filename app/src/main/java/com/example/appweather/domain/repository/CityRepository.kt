package com.example.appweather.domain.repository

import com.example.appweather.domain.model.City


interface CityRepository {
    suspend fun getCityByName(name: String): City?
    suspend fun getCityByCoordinates(lat: Double, lon: Double): City?
    suspend fun addCity(city: City): Int
    suspend fun removeCity(cityName: String)
    suspend fun getAllCities(): List<City>
    suspend fun getDefaultCity(): City?
    suspend fun saveDefaultCity(cityName: String)
}
