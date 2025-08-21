package com.example.appweather.domain.useCase

import com.example.appweather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(city: String) = repository.getWeather(city)

    suspend operator fun invoke(lat: Double, lon: Double) = repository.getWeather(lat, lon)
}
