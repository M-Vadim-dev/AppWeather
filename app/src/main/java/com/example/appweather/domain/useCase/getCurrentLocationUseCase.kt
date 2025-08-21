package com.example.appweather.domain.useCase

import android.location.Location
import com.example.appweather.domain.repository.LocationRepository
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(): Location? = repository.getCurrentLocation()
}
