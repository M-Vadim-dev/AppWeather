package com.example.appweather.domain.useCase

import com.example.appweather.domain.model.City
import com.example.appweather.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDefaultCityUseCase @Inject constructor(
    private val repository: CityRepository,
) {
    operator fun invoke(): Flow<City?> = repository.getDefaultCityFlow()
}
