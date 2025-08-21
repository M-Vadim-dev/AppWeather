package com.example.appweather.data.local

import androidx.room.TypeConverter
import com.example.appweather.data.local.jsonModel.ForecastItemEntity
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class Converters {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromForecastList(forecast: List<ForecastItemEntity>): String {
        return json.encodeToString(ListSerializer(ForecastItemEntity.serializer()), forecast)
    }

    @TypeConverter
    fun toForecastList(data: String): List<ForecastItemEntity> {
        return json.decodeFromString(ListSerializer(ForecastItemEntity.serializer()), data)
    }
}