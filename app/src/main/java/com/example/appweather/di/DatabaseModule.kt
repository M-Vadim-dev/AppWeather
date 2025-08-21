package com.example.appweather.di

import android.content.Context
import androidx.room.Room
import com.example.appweather.data.local.room.WeatherDatabase
import com.example.appweather.data.local.room.dao.CityDao
import com.example.appweather.data.local.room.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather_db"
        ).build()
    }

    @Provides
    fun provideWeatherDao(db: WeatherDatabase): WeatherDao = db.weatherDao()

    @Provides
    fun provideCityDao(db: WeatherDatabase): CityDao = db.cityDao()
}