package com.example.appweather.data.repository

import com.example.appweather.data.local.room.dao.CityDao
import com.example.appweather.data.mapper.toDomain
import com.example.appweather.data.mapper.toEntity
import com.example.appweather.domain.model.City
import com.example.appweather.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val cityDao: CityDao,
) : CityRepository {

    override suspend fun getCityByName(name: String): City? =
        cityDao.getCityByName(name)?.toDomain()

    override suspend fun getCityByCoordinates(lat: Double, lon: Double): City? =
        cityDao.getCityByCoordinates(lat, lon)?.toDomain()

    override suspend fun addCity(city: City): Int =
        cityDao.addCity(city.toEntity()).toInt()

    override suspend fun removeCity(cityName: String) {
        cityDao.getCityByName(cityName)?.let { cityDao.removeCity(it) }
    }

    override suspend fun getAllCities(): List<City> =
        cityDao.getAllCities().map { it.toDomain() }

    override suspend fun getDefaultCity(): City? =
        cityDao.getDefaultCity()?.toDomain()

    override fun getDefaultCityFlow(): Flow<City?> =
        cityDao.getDefaultCityFlow().map { it?.toDomain() }

    override suspend fun saveDefaultCity(cityName: String) =
        cityDao.setDefaultCity(cityName)
}
