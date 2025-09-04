package com.example.appweather.domain.useCase

import com.example.appweather.domain.model.WeatherInfo
import com.example.appweather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository,
) {

    suspend operator fun invoke(
        cityName: String? = null,
        lat: Double? = null,
        lon: Double? = null,
    ): WeatherInfo = repository.getWeather(cityName, lat, lon)

}