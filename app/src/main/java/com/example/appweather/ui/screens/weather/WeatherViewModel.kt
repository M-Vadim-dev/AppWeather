package com.example.appweather.ui.screens.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appweather.domain.model.City
import com.example.appweather.domain.model.WeatherInfo
import com.example.appweather.domain.useCase.EnsureDefaultCityUseCase
import com.example.appweather.domain.useCase.GetAllCitiesUseCase
import com.example.appweather.domain.useCase.GetDefaultCityUseCase
import com.example.appweather.domain.useCase.GetWeatherUseCase
import com.example.appweather.ui.screens.weather.mapper.toUi
import com.example.appweather.ui.screens.weather.mapper.toUiState
import com.example.appweather.utils.DateUtils.formatDate
import com.example.appweather.utils.DateUtils.formatWeekDay
import com.example.appweather.utils.DateUtils.parseApi
import com.example.appweather.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val ensureDefaultCityUseCase: EnsureDefaultCityUseCase,
    private val getDefaultCityUseCase: GetDefaultCityUseCase,
    private val getAllCitiesUseCase: GetAllCitiesUseCase,
    private val networkMonitor: NetworkMonitor,
) : ViewModel() {

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities

    private val _selectedCity = MutableStateFlow<City?>(null)
    val selectedCity: StateFlow<City?> = _selectedCity

    private val _weatherMap = MutableStateFlow<Map<City, WeatherUiState>>(emptyMap())
    val weatherMap: StateFlow<Map<City, WeatherUiState>> = _weatherMap

    val isConnected: StateFlow<Boolean> = networkMonitor.isConnected

    init {
        viewModelScope.launch {
            ensureDefaultCityUseCase()

            getDefaultCityUseCase().collect { defaultCity ->
                val cityList = getAllCitiesUseCase()
                val orderedCities = if (defaultCity != null) {
                    val otherCities = cityList.filter { it != defaultCity }
                    listOf(defaultCity) + otherCities
                } else cityList

                _cities.value = orderedCities

                val startCity = defaultCity ?: orderedCities.firstOrNull()
                startCity?.let { selectCity(it) }
            }
        }
    }

    internal fun refreshCityWeather(city: City) {
        viewModelScope.launch {
            _weatherMap.value = _weatherMap.value.toMutableMap().apply {
                put(city, _weatherMap.value[city]?.copy(isLoading = true) ?: WeatherUiState(isLoading = true))
            }

            try {
                val weather = getWeatherUseCase(city.name)
                val uiState = convertToUiState(weather)
                _weatherMap.value = _weatherMap.value.toMutableMap().apply {
                    put(city, uiState.copy(isLoading = false))
                }

                val defaultCity = getDefaultCityUseCase().firstOrNull()
                val cityList = getAllCitiesUseCase()
                val orderedCities = if (defaultCity != null) {
                    val otherCities = cityList.filter { it != defaultCity }
                    listOf(defaultCity) + otherCities
                } else cityList

                _cities.value = orderedCities

            } catch (e: Exception) {
                _weatherMap.value = _weatherMap.value.toMutableMap().apply {
                    put(
                        city, WeatherUiState(
                            errorMessage = e.message,
                            isNetworkAvailable = isConnected.value,
                            isLoading = false
                        )
                    )
                }
            }
        }
    }

    internal fun selectCity(city: City) {
        _selectedCity.value = city

        if (_weatherMap.value[city] != null) return

        refreshCityWeather(city)
    }

    private fun convertToUiState(weather: WeatherInfo): WeatherUiState {
        val parsedDateTime =
            parseApi(weather.localtime) ?: return WeatherUiState(errorMessage = "Invalid date")

        val allHours = weather.forecast.flatMap { it.hours }

        val filteredHourly = allHours.filter { hour ->
            parseApi(hour.time)?.isAfter(parsedDateTime) ?: false
        }

        val limitedHourly = filteredHourly.take(DEFAULT_HOURLY_LIMIT)

        val todayForecast = weather.forecast.find { it.date == weather.localtime.substring(0, 10) }

        return weather.toUiState().copy(
            dateText = formatDate(parsedDateTime),
            weekDayText = formatWeekDay(parsedDateTime),
            hourlyForecast = limitedHourly.map { it.toUi() },
            astroInfo = todayForecast?.astro?.toUi(),
            isNetworkAvailable = isConnected.value
        )
    }

    companion object {
        private const val DEFAULT_HOURLY_LIMIT = 24
    }
}
