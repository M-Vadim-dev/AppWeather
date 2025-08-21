package com.example.appweather.domain.useCase

import com.example.appweather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetAllCitiesUseCase @Inject constructor(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(): List<String> = repository.getAllCities()
}
