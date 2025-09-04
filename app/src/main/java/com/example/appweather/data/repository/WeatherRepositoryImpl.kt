package com.example.appweather.data.repository

import com.example.appweather.data.local.room.dao.WeatherDao
import com.example.appweather.data.mapper.WeatherDbConverter
import com.example.appweather.data.mapper.WeatherMapper
import com.example.appweather.data.remote.WeatherApiService
import com.example.appweather.data.remote.dto.ForecastResponse
import com.example.appweather.domain.model.City
import com.example.appweather.domain.model.WeatherInfo
import com.example.appweather.domain.repository.CityRepository
import com.example.appweather.domain.repository.WeatherRepository
import javax.inject.Inject
import kotlin.math.absoluteValue

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApiService,
    private val weatherDao: WeatherDao,
    private val cityRepository: CityRepository,
    private val weatherMapper: WeatherMapper,
    private val weatherDbConverter: WeatherDbConverter,
) : WeatherRepository {

    override suspend fun getWeather(cityName: String?, lat: Double?, lon: Double?): WeatherInfo {
        val locationQuery = when {
            cityName != null -> cityName
            lat != null && lon != null -> "$lat,$lon"
            else -> throw IllegalArgumentException("Need city name or coordinates")
        }

        val apiResponse = api.getWeatherForecast(locationQuery)

        // Создаем или получаем город из БД
        val city = getOrCreateCityFromResponse(apiResponse, cityName)

        return weatherMapper.toDomain(apiResponse, city.id)
    }

    override suspend fun getWeatherWithFallback(
        cityName: String?,
        lat: Double?,
        lon: Double?
    ): WeatherInfo {
        val city: City? = when {
            cityName != null -> cityRepository.getCityByName(cityName)
            lat != null && lon != null -> cityRepository.getCityByCoordinates(lat, lon)
            else -> null
        }

        if (city != null) return getWeatherForCity(city)

        // Если города нет в БД, используем обычный getWeather, который создаст город
        return getWeather(cityName, lat, lon)
    }

    private suspend fun getWeatherForCity(city: City): WeatherInfo {
        val cached = weatherDao.getLatestWeather(city.id)
        val now = System.currentTimeMillis()

        if (cached != null && now - cached.lastUpdatedEpoch < CACHE_DURATION_MS) {
            return weatherDbConverter.toDomain(cached, city.name, city.lat, city.lon)
        }

        val apiResponse = api.getWeatherForecast(city.name)
        val domainWeather = weatherMapper.toDomain(apiResponse, city.id)
        weatherDao.insertWeather(weatherDbConverter.toEntity(domainWeather))
        return domainWeather
    }

    private suspend fun getOrCreateCityFromResponse(response: ForecastResponse, customName: String?): City {
        val cityName = customName ?: response.location.name
        val lat = response.location.lat ?: 0.0
        val lon = response.location.lon ?: 0.0

        // Пытаемся найти город в БД
        var city = cityRepository.getCityByName(cityName)
        if (city == null && lat != 0.0 && lon != 0.0) {
            city = cityRepository.getCityByCoordinates(lat, lon)
        }

        // Если город не найден, создаем новый
        if (city == null) {
            city = City(
                id = generateCityId(cityName, lat, lon),
                name = cityName,
                lat = lat,
                lon = lon,
                isDefault = false
            )
            cityRepository.addCity(city)
        }

        return city
    }

    private fun generateCityId(name: String, lat: Double, lon: Double): Int {
        return (name.hashCode() + lat.hashCode() + lon.hashCode()).absoluteValue
    }

    companion object {
        private const val CACHE_DURATION_MS = 30 * 60 * 1000L
    }
}