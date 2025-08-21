package com.example.appweather.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appweather.data.local.room.dbo.WeatherEntity

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE city = :city")
    suspend fun getWeather(city: String): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(entity: WeatherEntity)
}