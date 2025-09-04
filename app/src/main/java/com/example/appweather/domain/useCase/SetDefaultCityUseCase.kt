package com.example.appweather.domain.useCase

import com.example.appweather.domain.repository.CityRepository
import javax.inject.Inject

class SetDefaultCityUseCase @Inject constructor(
    private val repository: CityRepository,
) {
    suspend operator fun invoke(city: String) = repository.saveDefaultCity(city)
}
