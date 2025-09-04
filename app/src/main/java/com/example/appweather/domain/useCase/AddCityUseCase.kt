package com.example.appweather.domain.useCase

import com.example.appweather.domain.model.City
import com.example.appweather.domain.repository.CityRepository
import javax.inject.Inject

class AddCityUseCase @Inject constructor(
    private val repository: CityRepository,
) {
    suspend operator fun invoke(city: City) = repository.addCity(city)
}
