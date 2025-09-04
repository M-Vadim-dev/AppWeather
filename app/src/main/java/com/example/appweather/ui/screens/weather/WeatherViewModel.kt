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

            val defaultCity = getDefaultCityUseCase()
            val cityList = getAllCitiesUseCase()
            _cities.value = cityList

            val startCity = defaultCity ?: cityList.firstOrNull()
            startCity?.let { selectCity(it) }
        }
    }

    internal fun refreshCityWeather(city: City) {
        viewModelScope.launch {
            try {
                val weather = getWeatherUseCase(city.name)
                val uiState = convertToUiState(weather)
                _weatherMap.value = _weatherMap.value.toMutableMap().apply {
                    put(city, uiState)
                }
            } catch (e: Exception) {
                _weatherMap.value = _weatherMap.value.toMutableMap().apply {
                    put(
                        city, WeatherUiState(
                            errorMessage = e.message,
                            isNetworkAvailable = isConnected.value
                        )
                    )
                }
            }
        }
    }

    internal fun selectCity(city: City) {
        _selectedCity.value = city

        if (_weatherMap.value[city] != null) return

        viewModelScope.launch {
            try {
                val weather = getWeatherUseCase(city.name)
                val uiState = convertToUiState(weather)
                _weatherMap.value = _weatherMap.value.toMutableMap().apply {
                    put(city, uiState)
                }
            } catch (e: Exception) {
                _weatherMap.value = _weatherMap.value.toMutableMap().apply {
                    put(
                        city, WeatherUiState(
                            errorMessage = e.message,
                            isNetworkAvailable = isConnected.value
                        )
                    )
                }
            }
        }
    }

    private fun convertToUiState(weather: WeatherInfo): WeatherUiState {
        val parsedDateTime =
            parseApi(weather.localtime) ?: return WeatherUiState(errorMessage = "Invalid date")

        val todayForecast = weather.forecast.find { it.date == weather.localtime.substring(0, 10) }
        val filteredHourly = todayForecast?.hours?.filter { hour ->
            parseApi(hour.time)?.toLocalTime()
                ?.let { it >= parsedDateTime.toLocalTime() } ?: false
        } ?: emptyList()

        return weather.toUiState().copy(
            dateText = formatDate(parsedDateTime),
            weekDayText = formatWeekDay(parsedDateTime),
            hourlyForecast = filteredHourly.map { it.toUi() },
            astroInfo = todayForecast?.astro?.toUi(),
            isNetworkAvailable = isConnected.value
        )
    }
}
