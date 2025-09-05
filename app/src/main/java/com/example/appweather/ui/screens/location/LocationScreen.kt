package com.example.appweather.ui.screens.location

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appweather.MainActivity
import com.example.appweather.R
import com.example.appweather.ui.theme.AppWeatherTheme

@SuppressLint("ContextCastToActivity")
@Composable
internal fun LocationScreen(
    modifier: Modifier = Modifier,
    viewModel: LocationViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val state = viewModel.state.collectAsState().value
    val searchQuery = viewModel.searchQuery.collectAsState().value

    val isGpsEnabled by viewModel.isGpsEnabled.collectAsState()
    val activity = LocalContext.current as MainActivity

    LaunchedEffect(Unit) {
        activity.checkGpsEnabled { enabled ->
            viewModel.setGpsEnabled(enabled)
        }
    }

    LocationScreenContent(
        modifier = modifier,
        state = state,
        onBackClick = onBackClick,
        onUpdateSearchQuery = { viewModel.onUpdateSearchQuery(it) },
        onSearch = viewModel::onSearchCity,
        onUseGps = { viewModel.useCurrentLocation() },
        onSelectCity = { viewModel.onSelectCity(it) },
        onRemoveCity = { viewModel.onRemoveCity(it) },
        onApplyDefaultCity = { viewModel.applyDefaultCity() },
        gpsEnabled = isGpsEnabled,
        onEditMode = { viewModel.toggleEditMode() },
        onSelectDefaultCandidate = { viewModel.onSelectDefaultCandidate(it) }
    )

}

@Composable
private fun LocationScreenContent(
    modifier: Modifier = Modifier,
    state: LocationUiState,
    onBackClick: () -> Unit,
    onSearch: (String) -> Unit,
    onUpdateSearchQuery: (String) -> Unit,
    onUseGps: () -> Unit,
    gpsEnabled: Boolean,
    onApplyDefaultCity: () -> Unit,
    onSelectCity: (String) -> Unit,
    onRemoveCity: (String) -> Unit,
    onEditMode: () -> Unit,
    onSelectDefaultCandidate: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            LocationTopAppBar(
                onBackClick = onBackClick,
                onUseGps = onUseGps,
                gpsEnabled = gpsEnabled,
                modifier = Modifier.height(80.dp)
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = stringResource(id = R.string.text_find_area_or_city),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
            )

            CitySearch(
                query = state.searchQuery,
                onQueryChange = onUpdateSearchQuery,
                onSearch = onSearch,
                onEditMode = onEditMode,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                isEditMode = state.isEditMode,
                onApplyDefaultCity = onApplyDefaultCity
            )

            val defaultCities = state.cities.filter { it.isDefault }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (defaultCities.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(id = R.string.text_current_location),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    items(defaultCities) { cityWeather ->
                        CityItem(
                            cityName = cityWeather.cityName,
                            temperature = cityWeather.temperature,
                            condition = cityWeather.condition,
                            localTime = cityWeather.localTime,
                            iconUrl = cityWeather.iconUrl,
                            isDay = cityWeather.isDay,
                            isDefault = cityWeather.isDefault,
                            isEditMode = state.isEditMode,
                            onSelect = { onSelectCity(cityWeather.cityName) },
                            pendingDefaultCity = state.pendingDefaultCity,
                            onSelectDefaultCandidate = { onSelectDefaultCandidate(it) }
                        )
                    }

                    item {
                        Text(
                            text = stringResource(id = R.string.text_added_locations),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                items(
                    items = state.cities.filter { !it.isDefault },
                    key = { it.cityName }
                ) { cityWeather ->
                    CustomSwipeBoxCityItem(
                        cityName = cityWeather.cityName,
                        temperature = cityWeather.temperature,
                        condition = cityWeather.condition,
                        localTime = cityWeather.localTime,
                        iconUrl = cityWeather.iconUrl,
                        isDay = cityWeather.isDay,
                        isDefault = cityWeather.isDefault,
                        isEditMode = state.isEditMode,
                        onSelectCity = onSelectCity,
                        onRemoveCity = onRemoveCity,
                        pendingDefaultCity = state.pendingDefaultCity,
                        onSelectDefaultCandidate = { onSelectDefaultCandidate(it) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationTopAppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onUseGps: () -> Unit,
    gpsEnabled: Boolean,
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(Alignment.CenterVertically)
            ) {
                Text(
                    text = stringResource(id = R.string.text_pick_location),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.text_back)
                )
            }
        },
        actions = {
            IconButton(
                onClick = onUseGps,
                enabled = gpsEnabled
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_gps_fixed),
                    contentDescription = stringResource(id = R.string.text_current_location),
                    tint = if (gpsEnabled) MaterialTheme.colorScheme.onPrimary else Color.Gray
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    )
}

@Composable
private fun CitySearch(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    isEditMode: Boolean,
    onEditMode: () -> Unit,
    onApplyDefaultCity: () -> Unit,

    ) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search_button),
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.size(24.dp)
                )

                BasicTextField(
                    value = query,
                    onValueChange = { if (it.length <= 25) onQueryChange(it) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    textStyle = TextStyle(
                        color = if (query.length > 24) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.surface,
                        fontSize = 16.sp
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search,
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = { onSearch(query) },
                        onDone = { onSearch(query) }
                    ),
                    cursorBrush = SolidColor(
                        if (query.length > 24) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.surface
                    ),
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.search_enter_location),
                                color = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        innerTextField()
                    }
                )

                if (query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.button_clear),
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onQueryChange("") }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable {
                    when {
                        isEditMode -> onApplyDefaultCity()
                        query.isEmpty() -> onEditMode()
                        else -> onSearch(query)
                    }
                }
                .background(MaterialTheme.colorScheme.onPrimary),
            contentAlignment = Alignment.Center
        ) {
            when {
                isEditMode ->
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = stringResource(id = R.string.set_as_default),
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                query.isEmpty() ->
                    Icon(
                        painter = painterResource(id = R.drawable.ic_map_marker_star),
                        contentDescription = stringResource(id = R.string.set_as_default),
                        modifier = Modifier.size(28.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                else ->
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = stringResource(id = R.string.text_added_locations),
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
            }

        }
    }

}

@Preview
@Composable
private fun LocationTopAppBarPreview() {
    AppWeatherTheme {
        LocationTopAppBar(
            onBackClick = { },
            onUseGps = { },
            gpsEnabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        )
    }
}

@Preview
@Composable
private fun CitySearchPreview() {
    AppWeatherTheme {
        CitySearch(
            query = stringResource(id = R.string.search_enter_location),
            onQueryChange = {},
            onSearch = {},
            onEditMode = {},
            isEditMode = false,
            onApplyDefaultCity = {}
        )
    }
}

@Preview(locale = "ru")
@Composable
private fun LocationScreenContentPreview() {
    AppWeatherTheme {
        LocationScreenContent(
            state = LocationUiState(
                searchQuery = "",
                cities = listOf(
                    CityWeatherUi(
                        cityName = "Москва",
                        temperature = "22°",
                        condition = "Солнечно",
                        iconUrl = "",
                        isDay = true,
                        isDefault = true,
                        localTime = "14:30"
                    ),
                    CityWeatherUi(
                        cityName = "Санкт-Петербург",
                        temperature = "18°",
                        condition = "Облачно",
                        iconUrl = "",
                        isDay = false,
                        isDefault = false,
                        localTime = "14:30"
                    ),
                    CityWeatherUi(
                        cityName = "Новосибирск",
                        temperature = "12°",
                        condition = "Дождь",
                        iconUrl = "",
                        isDay = true,
                        isDefault = false,
                        localTime = "18:00"
                    )
                )
            ),
            onBackClick = {},
            onSearch = {},
            onUpdateSearchQuery = {},
            onUseGps = {},
            gpsEnabled = true,
            onApplyDefaultCity = {},
            onSelectCity = {},
            onRemoveCity = {},
            onEditMode = {},
            onSelectDefaultCandidate = {}
        )
    }
}