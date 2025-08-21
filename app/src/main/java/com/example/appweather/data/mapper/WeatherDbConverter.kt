package com.example.appweather.data.mapper

import com.example.appweather.data.local.room.dbo.WeatherEntity
import com.example.appweather.domain.model.WeatherInfo
import javax.inject.Inject


class WeatherDbConverter @Inject constructor() {   //todo

    fun toEntity(domain: WeatherInfo): WeatherEntity {
        return WeatherEntity(
            city = domain.city,
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

    fun toDomain(entity: WeatherEntity): WeatherInfo {
        return WeatherInfo(
            city = entity.city,
            country = entity.country,
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
