package com.example.appweather.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appweather.data.local.room.dbo.WeatherEntity

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE cityId = :cityId")
    suspend fun getWeather(cityId: Int): WeatherEntity?

    @Query("SELECT * FROM weather WHERE cityId = :cityId ORDER BY lastUpdatedEpoch DESC LIMIT 1")
    suspend fun getLatestWeather(cityId: Int): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)
}