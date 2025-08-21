package com.example.appweather.domain.useCase

import com.example.appweather.domain.repository.WeatherRepository
import javax.inject.Inject

class SetDefaultCityUseCase @Inject constructor(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(city: String) = repository.saveDefaultCity(city)
}
