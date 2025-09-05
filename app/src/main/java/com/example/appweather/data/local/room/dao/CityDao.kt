package com.example.appweather.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appweather.data.local.room.dbo.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM cities WHERE id = :id LIMIT 1")
    suspend fun getCityById(id: Int): CityEntity?

    @Query("SELECT * FROM cities WHERE name = :name LIMIT 1")
    suspend fun getCityByName(name: String): CityEntity?

    @Query("SELECT * FROM cities WHERE lat = :lat AND lon = :lon LIMIT 1")
    suspend fun getCityByCoordinates(lat: Double, lon: Double): CityEntity?

    @Query("SELECT * FROM cities")
    suspend fun getAllCities(): List<CityEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCity(city: CityEntity): Long

    @Delete
    suspend fun removeCity(city: CityEntity)

    @Query("SELECT * FROM cities WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultCity(): CityEntity?

    @Query("SELECT * FROM cities WHERE isDefault = 1 LIMIT 1")
    fun getDefaultCityFlow(): Flow<CityEntity?>

    @Query("UPDATE cities SET isDefault = CASE WHEN name = :cityName THEN 1 ELSE 0 END")
    suspend fun setDefaultCity(cityName: String)
}