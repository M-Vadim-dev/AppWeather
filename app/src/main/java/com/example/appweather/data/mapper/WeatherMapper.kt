package com.example.appweather.data.mapper

import com.example.appweather.data.remote.dto.ConditionDto
import com.example.appweather.data.remote.dto.ForecastDayDto
import com.example.appweather.data.remote.dto.ForecastResponse
import com.example.appweather.data.remote.dto.HourDto
import com.example.appweather.domain.model.AstroInfo
import com.example.appweather.domain.model.ForecastItem
import com.example.appweather.domain.model.HourInfo
import com.example.appweather.domain.model.WeatherInfo
import javax.inject.Inject

class WeatherMapper @Inject constructor() {

    fun toDomain(dto: ForecastResponse): WeatherInfo {
        val todayForecast = dto.forecast.forecastDay.firstOrNull()

        return WeatherInfo(
            city = dto.location.name,
            country = dto.location.country,
            localtime = dto.location.localtime,
            temperatureC = dto.current.tempC,
            temperatureF = dto.current.tempF,
            condition = dto.current.condition.text,
            iconUrl = dto.current.condition.toDomainIconUrl(),
            isDay = dto.current.isDay == 1,
            humidity = dto.current.humidity,
            windKph = dto.current.windKph,
            windMph = dto.current.windMph,
            windDir = dto.current.windDir,
            uvIndex = dto.current.uv,
            cloud = dto.current.cloud,
            sunrise = todayForecast?.astro?.sunrise ?: "N/A",
            sunset = todayForecast?.astro?.sunset ?: "N/A",
            moonPhase = todayForecast?.astro?.moonPhase ?: "N/A",
            moonIllumination = todayForecast?.astro?.moonIllumination ?: "N/A",
            forecast = dto.forecast.forecastDay.map { it.toDomain() },
            lastUpdatedEpoch = dto.current.lastUpdatedEpoch,
        )
    }

    private fun ForecastDayDto.toDomain(): ForecastItem {
        return ForecastItem(
            date = date,
            dateEpoch = dateEpoch,
            minTempC = day.minTempC,
            maxTempC = day.maxTempC,
            avgTempC = day.avgTempC,
            maxWindKph = day.maxWindKph,
            maxWindMph = day.maxWindMph,
            totalPrecipMm = day.totalPrecipMm,
            totalSnowCm = day.totalSnowCm,
            avgHumidity = day.avgHumidity,
            dailyWillItRain = day.dailyWillItRain,
            dailyChanceOfRain = day.dailyChanceOfRain,
            dailyWillItSnow = day.dailyWillItSnow,
            dailyChanceOfSnow = day.dailyChanceOfSnow,
            condition = day.condition.text,
            iconUrl = day.condition.toDomainIconUrl(),
            astro = AstroInfo(
                sunrise = astro.sunrise,
                sunset = astro.sunset,
                moonrise = astro.moonrise,
                moonset = astro.moonset,
                moonPhase = astro.moonPhase,
                moonIllumination = astro.moonIllumination
            ),
            hours = hour.map { it.toDomain() }
        )
    }

    private fun HourDto.toDomain(): HourInfo {
        return HourInfo(
            time = time,
            timeEpoch = timeEpoch,
            tempC = tempC,
            tempF = tempF,
            isDay = isDay == 1,
            condition = condition.text,
            iconUrl = condition.toDomainIconUrl(),
            windMph = windMph,
            windKph = windKph,
            windDir = windDir,
            humidity = humidity,
            cloud = cloud,
            feelsLikeC = feelsLikeC,
            feelsLikeF = feelsLikeF,
            windChillC = windChillC,
            heatIndexC = heatIndexC,
            dewPointC = dewPointC,
            willItRain = willItRain == 1,
            chanceOfRain = chanceOfRain,
            willItSnow = willItSnow == 1,
            chanceOfSnow = chanceOfSnow,
            visKm = visKm,
            uv = uv
        )
    }

    private fun ConditionDto.toDomainIconUrl(): String = "$ICON_BASE_URL$icon"

    companion object {
        private const val ICON_BASE_URL = "https:"
    }
}