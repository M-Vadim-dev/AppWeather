package com.example.appweather.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    val location: LocationDto,
    val current: CurrentDto,
    val forecast: ForecastDto,
)

@Serializable
data class LocationDto(
    val name: String,
    val region: String? = null,
    val country: String,
    val lat: Double? = null,
    val lon: Double? = null,
    @SerialName("tz_id")
    val tz: String? = null,
    val localtime: String,
)

@Serializable
data class CurrentDto(
    @SerialName("last_updated_epoch")
    val lastUpdatedEpoch: Long,
    @SerialName("temp_c")
    val tempC: Double,
    @SerialName("temp_f")
    val tempF: Double,
    @SerialName("is_day")
    val isDay: Int,
    val condition: ConditionDto,
    @SerialName("wind_mph")
    val windMph: Double,
    @SerialName("wind_kph")
    val windKph: Double,
    @SerialName("wind_dir")
    val windDir: String,
    val cloud: Int,
    val humidity: Int,
    val uv: Double,
)

@Serializable
data class ConditionDto(
    val text: String,
    val icon: String,
    val code: Int? = null,
)

@Serializable
data class ForecastDto(
    @SerialName("forecastday")
    val forecastDay: List<ForecastDayDto>,
)

@Serializable
data class ForecastDayDto(
    val date: String,
    @SerialName("date_epoch")
    val dateEpoch: Long,
    val day: DayDto,
    val astro: AstroDto,
    val hour: List<HourDto>,
)

@Serializable
data class DayDto(
    @SerialName("maxtemp_c")
    val maxTempC: Double,
    @SerialName("mintemp_c")
    val minTempC: Double,
    @SerialName("avgtemp_c")
    val avgTempC: Double,
    @SerialName("maxwind_mph")
    val maxWindMph: Double,
    @SerialName("maxwind_kph")
    val maxWindKph: Double,
    @SerialName("totalprecip_mm")
    val totalPrecipMm: Double,
    @SerialName("totalsnow_cm")
    val totalSnowCm: Double,
    @SerialName("avghumidity")
    val avgHumidity: Double,
    @SerialName("daily_will_it_rain")
    val dailyWillItRain: Int,
    @SerialName("daily_chance_of_rain")
    val dailyChanceOfRain: Int,
    @SerialName("daily_will_it_snow")
    val dailyWillItSnow: Int,
    @SerialName("daily_chance_of_snow")
    val dailyChanceOfSnow: Int,
    val condition: ConditionDto,
)

@Serializable
data class AstroDto(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    @SerialName("moon_phase")
    val moonPhase: String,
    @SerialName("moon_illumination")
    val moonIllumination: String,
)

@Serializable
data class HourDto(
    val time: String,
    @SerialName("time_epoch")
    val timeEpoch: Long,
    @SerialName("temp_c")
    val tempC: Double,
    @SerialName("temp_f")
    val tempF: Double,
    @SerialName("is_day")
    val isDay: Int,
    val condition: ConditionDto,
    @SerialName("wind_mph")
    val windMph: Double,
    @SerialName("wind_kph")
    val windKph: Double,
    @SerialName("wind_dir")
    val windDir: String,
    val humidity: Int,
    val cloud: Int,
    @SerialName("feelslike_c")
    val feelsLikeC: Double,
    @SerialName("feelslike_f")
    val feelsLikeF: Double,
    @SerialName("windchill_c")
    val windChillC: Double,
    @SerialName("heatindex_c")
    val heatIndexC: Double,
    @SerialName("dewpoint_c")
    val dewPointC: Double,
    @SerialName("will_it_rain")
    val willItRain: Int,
    @SerialName("chance_of_rain")
    val chanceOfRain: Int,
    @SerialName("will_it_snow")
    val willItSnow: Int,
    @SerialName("chance_of_snow")
    val chanceOfSnow: Int,
    @SerialName("vis_km")
    val visKm: Double,
    val uv: Double
)
