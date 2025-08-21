package com.example.appweather.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appweather.domain.model.AstroInfo
import com.example.appweather.domain.model.ForecastItem
import com.example.appweather.domain.model.HourInfo
import com.example.appweather.domain.model.WeatherInfo
import com.example.appweather.domain.useCase.AddCityUseCase
import com.example.appweather.domain.useCase.EnsureDefaultCityUseCase
import com.example.appweather.domain.useCase.GetAllCitiesUseCase
import com.example.appweather.domain.useCase.GetCurrentLocationUseCase
import com.example.appweather.domain.useCase.GetDefaultCityUseCase
import com.example.appweather.domain.useCase.GetWeatherUseCase
import com.example.appweather.domain.useCase.RemoveCityUseCase
import com.example.appweather.domain.useCase.SaveDefaultCityUseCase
import com.example.appweather.utils.DateUtils.formatDate
import com.example.appweather.utils.DateUtils.formatWeekDay
import com.example.appweather.utils.DateUtils.parseApiDateTime
import com.example.appweather.utils.DateUtils.parseHourTimeFromApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class WeatherState(
    val isLoading: Boolean = false,
    val weatherInfo: WeatherInfo? = null,
    val todayForecast: ForecastItem? = null,
    val hourlyForecast: List<HourInfo> = emptyList(),
    val astroInfo: AstroInfo? = null,
    val errorMessage: String? = null,
    val timeToday: LocalDateTime = LocalDateTime.now(),
    val dateText: String = "",
    val weekDayText: String = "",

    )

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val ensureDefaultCityUseCase: EnsureDefaultCityUseCase,
    private val getDefaultCityUseCase: GetDefaultCityUseCase,
    private val saveDefaultCityUseCase: SaveDefaultCityUseCase,
    private val addCityUseCase: AddCityUseCase,
    private val removeCityUseCase: RemoveCityUseCase,
    private val getAllCitiesUseCase: GetAllCitiesUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state

    private val _cities = MutableStateFlow<List<String>>(emptyList())
    val cities: StateFlow<List<String>> = _cities

    private val _selectedCity = MutableStateFlow<String?>(null)
    val selectedCity: StateFlow<String?> = _selectedCity

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isSearchVisible = MutableStateFlow(false)
    val isSearchVisible: StateFlow<Boolean> = _isSearchVisible

    init {
        viewModelScope.launch {
            ensureDefaultCityUseCase()

            val city = getDefaultCityUseCase()
            _selectedCity.value = city

            city?.let {
                loadWeather(it)
            }

            loadCities()
        }
    }


    fun toggleSearchVisibility() {
        _isSearchVisible.value = !_isSearchVisible.value
    }

    fun useCurrentLocation() {
        viewModelScope.launch {
            val location = getCurrentLocationUseCase()
            location?.let { loc ->
                _selectedCity.value = null
                loadWeather(loc.latitude, loc.longitude)
            }
        }
    }

    fun loadWeather(city: String) {
        viewModelScope.launch {
            _state.value = WeatherState(isLoading = true)
            try {
                val weather = getWeatherUseCase(city)
                val parsedDate = parseApiDateTime(weather.localtime)
                val currentTime = parsedDate.toLocalTime()

                val todayForecast = weather.forecast.find { forecast ->
                    forecast.date == parsedDate.toLocalDate().toString()
                }

                val filteredHourlyForecast = todayForecast?.hours?.filter { hourInfo ->
                    val hourTime = parseHourTimeFromApi(hourInfo.time)
                    hourTime >= currentTime
                } ?: emptyList()

                _state.value = WeatherState(
                    weatherInfo = weather,
                    dateText = formatDate(parsedDate),
                    weekDayText = formatWeekDay(parsedDate),
                    todayForecast = todayForecast,
                    hourlyForecast = filteredHourlyForecast,
                    astroInfo = todayForecast?.astro
                )
            } catch (e: Exception) {
                _state.value = WeatherState(errorMessage = e.message)
            }
        }
    }

    fun loadWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _state.value = WeatherState(isLoading = true)
            try {
                val weather = getWeatherUseCase(lat, lon)
                _state.value = WeatherState(weatherInfo = weather)
            } catch (e: Exception) {
                _state.value = WeatherState(errorMessage = e.message)
            }
        }
    }

    fun refresh(city: String) {
        viewModelScope.launch {
            _state.value = WeatherState(isLoading = true)
            try {
                _state.value = WeatherState(weatherInfo = getWeatherUseCase(city))
            } catch (e: Exception) {
                _state.value = WeatherState(errorMessage = e.message)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchCity() {
        val city = _searchQuery.value.trim()
        if (city.isNotEmpty()) {
            selectCity(city)
        }
    }

    fun selectCity(city: String) {
        _selectedCity.value = city
        viewModelScope.launch {
            saveDefaultCityUseCase(city)
            loadWeather(city)
        }
    }

    fun addCity(city: String) {
        viewModelScope.launch {
            addCityUseCase(city)
            loadCities()
        }
    }

    fun removeCity(city: String) {
        viewModelScope.launch {
            removeCityUseCase(city)
            loadCities()
        }
    }

    fun setDefaultCity(city: String) {
        viewModelScope.launch {
            saveDefaultCityUseCase(city)
        }
    }

    private fun loadCities() {
        viewModelScope.launch {
            _cities.value = getAllCitiesUseCase()
        }
    }
}
