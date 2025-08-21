package com.example.appweather.di

import com.example.appweather.data.local.room.dao.CityDao
import com.example.appweather.data.mapper.WeatherMapper
import com.example.appweather.data.remote.WeatherApiService
import com.example.appweather.data.repository.WeatherRepositoryImpl
import com.example.appweather.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        api: WeatherApiService,
        cityDao: CityDao,
        mapper: WeatherMapper,
    ): WeatherRepository = WeatherRepositoryImpl(
        api = api,
        cityDao = cityDao,
        weatherMapper = mapper,
    )

}
