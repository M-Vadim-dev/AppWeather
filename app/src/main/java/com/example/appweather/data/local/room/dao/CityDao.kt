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
    @Query("SELECT name FROM cities")
    suspend fun getAllCities(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCity(city: CityEntity)

    @Delete
    suspend fun removeCity(city: CityEntity)

    @Query("SELECT name FROM cities WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultCity(): String?

    @Query("UPDATE cities SET isDefault = CASE WHEN name = :cityName THEN 1 ELSE 0 END")
    suspend fun setDefaultCity(cityName: String)
}