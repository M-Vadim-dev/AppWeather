package com.example.appweather.data.mapper

import com.example.appweather.data.local.room.dbo.WeatherEntity
import com.example.appweather.domain.model.WeatherInfo
import javax.inject.Inject

class WeatherDbConverter @Inject constructor() {

    fun toEntity(domain: WeatherInfo): WeatherEntity {
        return WeatherEntity(
            cityId = domain.cityId,
            country = domain.country,
            localtime = domain.localtime,
            temperatureC = domain.temperatureC,
            temperatureF = domain.temperatureF,
            condition = domain.condition,
            iconUrl = domain.iconUrl,
            windKph = domain.windKph,
            windMph = domain.windMph,
            windDir = domain.windDir,
            humidity = domain.humidity,
            uvIndex = domain.uvIndex,
            pressure = domain.pressure,
            cloud = domain.cloud,
            sunrise = domain.sunrise,
            sunset = domain.sunset,
            moonPhase = domain.moonPhase,
            moonIllumination = domain.moonIllumination,
            isDay = domain.isDay,
            forecast = domain.forecast.map { it.toEntity() },
            lastUpdatedEpoch = domain.lastUpdatedEpoch,
        )
    }

    fun toDomain(entity: WeatherEntity, cityName: String, lat: Double, lon: Double): WeatherInfo {
        return WeatherInfo(
            cityId = entity.cityId,
            cityName = cityName,
            country = entity.country,
            latitude = lat,
            longitude = lon,
            localtime = entity.localtime,
            temperatureC = entity.temperatureC,
            temperatureF = entity.temperatureF,
            condition = entity.condition,
            iconUrl = entity.iconUrl,
            windKph = entity.windKph,
            windMph = entity.windMph,
            windDir = entity.windDir,
            humidity = entity.humidity,
            uvIndex = entity.uvIndex,
            pressure = entity.pressure,
            cloud = entity.cloud,
            sunrise = entity.sunrise,
            sunset = entity.sunset,
            moonPhase = entity.moonPhase,
            moonIllumination = entity.moonIllumination,
            forecast = entity.forecast.map { it.toDomain() },
            isDay = entity.isDay,
            lastUpdatedEpoch = entity.lastUpdatedEpoch,
        )
    }
}
