package com.example.appweather.domain.useCase

import com.example.appweather.domain.model.City
import com.example.appweather.domain.repository.CityRepository
import javax.inject.Inject

class GetAllCitiesUseCase @Inject constructor(
    private val repository: CityRepository,
) {
    suspend operator fun invoke(): List<City> = repository.getAllCities()
}
