package com.example.appweather.data.mapper


import com.example.appweather.data.local.jsonModel.AstroEntity
import com.example.appweather.data.local.jsonModel.ForecastItemEntity
import com.example.appweather.data.local.jsonModel.HourEntity
import com.example.appweather.domain.model.AstroInfo
import com.example.appweather.domain.model.ForecastItem
import com.example.appweather.domain.model.HourInfo

fun ForecastItem.toEntity(): ForecastItemEntity =
    ForecastItemEntity(
        date = date,
        dateEpoch = dateEpoch,
        minTempC = minTempC,
        maxTempC = maxTempC,
        avgTempC = avgTempC,
        maxWindKph = maxWindKph,
        maxWindMph = maxWindMph,
        totalPrecipMm = totalPrecipMm,
        totalSnowCm = totalSnowCm,
        avgHumidity = avgHumidity,
        dailyWillItRain = dailyWillItRain,
        dailyChanceOfRain = dailyChanceOfRain,
        dailyWillItSnow = dailyWillItSnow,
        dailyChanceOfSnow = dailyChanceOfSnow,
        condition = condition,
        iconUrl = iconUrl,
        astro = AstroEntity(
            sunrise = astro.sunrise,
            sunset = astro.sunset,
            moonrise = astro.moonrise,
            moonset = astro.moonset,
            moonPhase = astro.moonPhase,
            moonIllumination = astro.moonIllumination
        ),
        hours = hours.map { it.toEntity() }
    )

fun ForecastItemEntity.toDomain(): ForecastItem =
    ForecastItem(
        date = date,
        dateEpoch = dateEpoch,
        minTempC = minTempC,
        maxTempC = maxTempC,
        avgTempC = avgTempC,
        maxWindKph = maxWindKph,
        maxWindMph = maxWindMph,
        totalPrecipMm = totalPrecipMm,
        totalSnowCm = totalSnowCm,
        avgHumidity = avgHumidity,
        dailyWillItRain = dailyWillItRain,
        dailyChanceOfRain = dailyChanceOfRain,
        dailyWillItSnow = dailyWillItSnow,
        dailyChanceOfSnow = dailyChanceOfSnow,
        condition = condition,
        iconUrl = iconUrl,
        astro = AstroInfo(
            sunrise = astro.sunrise,
            sunset = astro.sunset,
            moonrise = astro.moonrise,
            moonset = astro.moonset,
            moonPhase = astro.moonPhase,
            moonIllumination = astro.moonIllumination
        ),
        hours = hours.map { it.toDomain() }
    )

fun HourInfo.toEntity(): HourEntity =
    HourEntity(
        time = time,
        timeEpoch = timeEpoch,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        condition = condition,
        iconUrl = iconUrl,
        windKph = windKph,
        windMph = windMph,
        windDir = windDir,
        humidity = humidity,
        cloud = cloud,
        feelsLikeC = feelsLikeC,
        feelsLikeF = feelsLikeF,
        windChillC = windChillC,
        heatIndexC = heatIndexC,
        dewPointC = dewPointC,
        willItRain = willItRain,
        chanceOfRain = chanceOfRain,
        willItSnow = willItSnow,
        chanceOfSnow = chanceOfSnow,
        visKm = visKm,
        uv = uv
    )

fun HourEntity.toDomain(): HourInfo =
    HourInfo(
        time = time,
        timeEpoch = timeEpoch,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        condition = condition,
        iconUrl = iconUrl,
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
        willItRain = willItRain,
        chanceOfRain = chanceOfRain,
        willItSnow = willItSnow,
        chanceOfSnow = chanceOfSnow,
        visKm = visKm,
        uv = uv
    )
