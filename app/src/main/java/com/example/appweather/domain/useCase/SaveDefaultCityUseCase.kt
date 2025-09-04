package com.example.appweather.domain.useCase

import com.example.appweather.domain.repository.CityRepository
import javax.inject.Inject

class SaveDefaultCityUseCase @Inject constructor(
    private val repository: CityRepository,
) {
    suspend operator fun invoke(cityName: String) = repository.saveDefaultCity(cityName)
}
