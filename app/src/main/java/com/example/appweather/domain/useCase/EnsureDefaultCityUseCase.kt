package com.example.appweather.domain.useCase

import com.example.appweather.data.mapper.WeatherMapper
import com.example.appweather.data.remote.WeatherApiService
import com.example.appweather.domain.model.City
import com.example.appweather.domain.repository.CityRepository
import javax.inject.Inject

class EnsureDefaultCityUseCase @Inject constructor(
    private val cityRepository: CityRepository,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val weatherApiService: WeatherApiService,
    private val weatherMapper: WeatherMapper,
) {

    suspend operator fun invoke() {
        val defaultCity = cityRepository.getDefaultCity()
        if (defaultCity != null) return

        val location = getCurrentLocationUseCase()
        if (location != null) {
            val apiResponse =
                weatherApiService.getWeatherForecast("${location.latitude},${location.longitude}")
            val weatherInfo = weatherMapper.toDomain(apiResponse, cityId = 0)

            val cityFromApi = City(
                id = 0,
                name = weatherInfo.cityName,
                lat = location.latitude,
                lon = location.longitude,
                isDefault = true
            )

            cityRepository.addCity(cityFromApi)
            cityRepository.saveDefaultCity(cityFromApi.name)
            cityFromApi
        } else {
            val cityFromDefault = City(
                id = 0,
                name = DEFAULT_CITY,
                lat = DEFAULT_LAT,
                lon = DEFAULT_LON,
                isDefault = true
            )

            cityRepository.addCity(cityFromDefault)
            cityRepository.saveDefaultCity(cityFromDefault.name)
            cityFromDefault
        }
    }

    companion object {
        private const val DEFAULT_CITY = "Moscow"
        private const val DEFAULT_LAT = 55.7558
        private const val DEFAULT_LON = 37.6173
    }
}
