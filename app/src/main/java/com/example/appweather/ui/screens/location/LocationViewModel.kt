package com.example.appweather.ui.screens.location

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appweather.domain.model.City
import com.example.appweather.domain.useCase.AddCityUseCase
import com.example.appweather.domain.useCase.GetAllCitiesUseCase
import com.example.appweather.domain.useCase.GetCurrentLocationUseCase
import com.example.appweather.domain.useCase.GetWeatherUseCase
import com.example.appweather.domain.useCase.RemoveCityUseCase
import com.example.appweather.domain.useCase.SaveDefaultCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CityWeatherUi(
    val cityName: String = "",
    val temperature: String = "",
    val condition: String = "",
    val iconUrl: String = "",
    val isDay: Boolean = true,
    val isDefault: Boolean = false,
    val localTime: String,
)

data class LocationUiState(
    val cities: List<CityWeatherUi> = emptyList(),
    val selectedCity: String? = null,
    val searchQuery: String = "",
    val isEditMode: Boolean = false,
    val pendingDefaultCity: String? = null,
)

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val getAllCitiesUseCase: GetAllCitiesUseCase,
    private val addCityUseCase: AddCityUseCase,
    private val removeCityUseCase: RemoveCityUseCase,
    private val saveDefaultCityUseCase: SaveDefaultCityUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LocationUiState())
    val state: StateFlow<LocationUiState> = _state

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isGpsEnabled = MutableStateFlow(false)
    val isGpsEnabled: StateFlow<Boolean> = _isGpsEnabled


    init {
        loadCitiesWithWeather()
    }

    fun onSearchCity(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            try {
                val weather = getWeatherUseCase(cityName = query)

                val city = City(
                    id = weather.cityId,
                    name = weather.cityName,
                    lat = 0.0,
                    lon = 0.0,
                    isDefault = false
                )

                addCityUseCase(city)
                loadCitiesWithWeather()

                _state.value = _state.value.copy(searchQuery = "")
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при поиске/добавлении города: ${e.message}")
            }
        }
    }

    fun onSelectDefaultCandidate(cityName: String) {
        _state.value = _state.value.copy(pendingDefaultCity = cityName)
    }

    fun applyDefaultCity() {
        viewModelScope.launch {
            state.value.pendingDefaultCity?.let { cityName ->
                saveDefaultCityUseCase(cityName)
                _state.value = _state.value.copy(
                    cities = _state.value.cities.map { it.copy(isDefault = it.cityName == cityName) },
                    selectedCity = cityName,
                    pendingDefaultCity = null,
                    isEditMode = false
                )
            }
        }
    }


    fun toggleEditMode() {
        _state.value = _state.value.copy(
            isEditMode = !_state.value.isEditMode
        )
    }

    fun setGpsEnabled(enabled: Boolean) {
        _isGpsEnabled.value = enabled
    }

    fun onUpdateSearchQuery(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
    }

    fun onSelectCity(cityName: String) {
        _state.value = _state.value.copy(selectedCity = cityName)
    }

    fun onSetDefaultCity(cityName: String) {
        viewModelScope.launch {
            try {
                saveDefaultCityUseCase(cityName)

                _state.value = _state.value.copy(
                    cities = _state.value.cities.map { city ->
                        city.copy(isDefault = city.cityName == cityName)
                    },
                    selectedCity = cityName
                )
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при установке дефолтного города: ${e.message}")
            }
        }
    }
    fun onRemoveCity(cityName: String) {
        viewModelScope.launch {
            removeCityUseCase(cityName)
            _state.value = _state.value.copy(
                cities = _state.value.cities.filterNot { it.cityName == cityName }
            )
        }
    }

    fun useCurrentLocation() {
        viewModelScope.launch {
            val location = getCurrentLocationUseCase()
            location?.let { loc ->
                try {
                    val weather = getWeatherUseCase(lat = loc.latitude, lon = loc.longitude)

                    val city = City(
                        id = weather.cityId,
                        name = weather.cityName,
                        lat = loc.latitude,
                        lon = loc.longitude,
                        isDefault = false
                    )
                    addCityUseCase(city)

                    loadCitiesWithWeather()

                } catch (e: Exception) {
                    Log.e(TAG, "Ошибка при получении погоды: ${e.message}")
                }
            }
        }
    }

    private fun loadCitiesWithWeather() {
        viewModelScope.launch {
            val cities = getAllCitiesUseCase()

            cities.map { city ->
                launch {
                    val weather = getWeatherUseCase(cityName = city.name)
                    val cityWeather = CityWeatherUi(
                        cityName = city.name,
                        temperature = "${weather.temperatureC.toInt()}°",
                        condition = weather.condition,
                        iconUrl = weather.iconUrl,
                        isDay = weather.isDay,
                        isDefault = city.isDefault,
                        localTime = weather.localtime
                    )

                    _state.value = _state.value.copy(
                        cities = _state.value.cities
                            .filter { it.cityName != cityWeather.cityName } + cityWeather
                    )
                }
            }
        }
    }

    companion object {
        private const val TAG = "LocationViewModel"
    }
}
