package com.example.appweather.data.local.room.dbo

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.appweather.data.local.jsonModel.ForecastItemEntity

@Entity(
    tableName = "weather",
    foreignKeys = [
        ForeignKey(
            entity = CityEntity::class,
            parentColumns = ["id"],
            childColumns = ["cityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cityId: Int,
    val country: String,
    val localtime: String,
    val temperatureC: Double,
    val temperatureF: Double,
    val condition: String,
    val iconUrl: String,
    val windKph: Double,
    val windMph: Double,
    val windDir: String,
    val humidity: Int,
    val uvIndex: Double,
    val pressure: Double,
    val cloud: Int,
    val sunrise: String,
    val sunset: String,
    val moonPhase: String,
    val moonIllumination: String,
    val isDay: Boolean,
    val forecast: List<ForecastItemEntity>,
    val lastUpdatedEpoch: Long,
)