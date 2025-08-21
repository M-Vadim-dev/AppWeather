package com.example.appweather.data.repository

import com.example.appweather.data.local.room.dao.CityDao
import com.example.appweather.data.local.room.dbo.CityEntity
import com.example.appweather.data.mapper.WeatherMapper
import com.example.appweather.data.remote.WeatherApiService
import com.example.appweather.domain.model.WeatherInfo
import com.example.appweather.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApiService,
    private val cityDao: CityDao,
    private val weatherMapper: WeatherMapper,
) : WeatherRepository {

    override suspend fun getWeather(city: String): WeatherInfo =
        weatherMapper.toDomain(api.getWeatherForecast(city))

    override suspend fun getWeather(lat: Double, lon: Double): WeatherInfo =
        weatherMapper.toDomain(api.getWeatherForecast("$lat,$lon"))

    override suspend fun saveDefaultCity(city: String) {
        cityDao.setDefaultCity(city)
    }

    override suspend fun getDefaultCity(): String? = cityDao.getDefaultCity()

    override suspend fun getAllCities(): List<String> = cityDao.getAllCities()

    override suspend fun addCity(city: String) {
        cityDao.addCity(CityEntity(name = city))
    }

    override suspend fun removeCity(city: String) {
        cityDao.removeCity(CityEntity(name = city))
    }

}
