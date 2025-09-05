package com.example.appweather.ui.screens.weather.mapper

import com.example.appweather.domain.model.AstroInfo
import com.example.appweather.domain.model.ForecastItem
import com.example.appweather.domain.model.HourInfo
import com.example.appweather.domain.model.WeatherInfo
import com.example.appweather.ui.screens.weather.AstroUiItem
import com.example.appweather.ui.screens.weather.ForecastUiItem
import com.example.appweather.ui.screens.weather.HourlyForecastUiItem
import com.example.appweather.ui.screens.weather.WeatherUiState
import com.example.appweather.utils.DateUtils.formatDateShortFromApi
import com.example.appweather.utils.DateUtils.formatTimeHHmm
import kotlin.math.roundToInt

internal fun WeatherInfo.toUiState(): WeatherUiState = WeatherUiState(
    cityName = cityName,
    country = country,
    temperature = temperatureC.toInt(),
    condition = condition,
    iconUrl = iconUrl,
    isDay = isDay,
    humidity = humidity,
    windSpeed = windKph,
    windDir = windDir,
    uvIndex = uvIndex,
    pressure = pressure,
    cloud = cloud,
    forecastDays = forecast.map { it.toUi() }
)

internal fun ForecastItem.toUi(): ForecastUiItem = ForecastUiItem(
    date = formatDateShortFromApi(date),
    iconUrl = iconUrl,
    condition = condition,
    minTemp = minTempC.toInt(),
    maxTemp = maxTempC.toInt(),
    avgTemp = avgTempC.toInt(),
)

internal fun HourInfo.toUi(): HourlyForecastUiItem = HourlyForecastUiItem(
    time = formatTimeHHmm(time),
    temperature = tempC.roundToInt(),
    iconUrl = iconUrl,
    condition = condition,
    humidity = humidity,
    windSpeed = windKph,
    windDir = windDir,
)

internal fun AstroInfo.toUi(): AstroUiItem = AstroUiItem(
    sunrise = sunrise,
    sunset = sunset,
    moonrise = moonrise,
    moonset = moonset,
    moonPhase = moonPhase,
    moonIllumination = moonIllumination,
)
