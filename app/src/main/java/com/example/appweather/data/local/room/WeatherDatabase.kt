package com.example.appweather.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appweather.data.local.Converters
import com.example.appweather.data.local.room.dao.CityDao
import com.example.appweather.data.local.room.dao.WeatherDao
import com.example.appweather.data.local.room.dbo.CityEntity
import com.example.appweather.data.local.room.dbo.WeatherEntity

@Database(
    entities = [WeatherEntity::class, CityEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun cityDao(): CityDao
}