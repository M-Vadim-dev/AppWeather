package com.example.appweather.data.mapper

import com.example.appweather.data.local.room.dbo.CityEntity
import com.example.appweather.domain.model.City

fun CityEntity.toDomain(): City =
    City(
        id = id,
        name = name,
        lat = lat,
        lon = lon,
        isDefault = isDefault,
    )

fun City.toEntity(): CityEntity =
    CityEntity(
        id = id,
        name = name,
        lat = lat,
        lon = lon,
        isDefault = isDefault,
    )
