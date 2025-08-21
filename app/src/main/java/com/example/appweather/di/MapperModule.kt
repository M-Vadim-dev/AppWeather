package com.example.appweather.di

import com.example.appweather.data.mapper.WeatherDbConverter
import com.example.appweather.data.mapper.WeatherMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Provides
    @Singleton
    fun provideWeatherMapper(): WeatherMapper = WeatherMapper()

    @Provides
    @Singleton
    fun provideWeatherDbConverter(): WeatherDbConverter = WeatherDbConverter()
}