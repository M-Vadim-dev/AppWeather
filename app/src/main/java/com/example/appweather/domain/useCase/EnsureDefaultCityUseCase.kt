package com.example.appweather.domain.useCase

import com.example.appweather.domain.repository.WeatherRepository
import javax.inject.Inject

class EnsureDefaultCityUseCase @Inject constructor(
    private val repository: WeatherRepository,
) {

    suspend operator fun invoke() {
        val defaultCity = repository.getDefaultCity()
        if (defaultCity == null) {
            repository.addCity(DEFAULT_CITY)
            repository.saveDefaultCity(DEFAULT_CITY)
        }
    }

    companion object {
        private const val DEFAULT_CITY = "USA"
    }
}
