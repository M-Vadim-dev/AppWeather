package com.example.appweather.ui.screens.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.appweather.R
import com.example.appweather.domain.model.City
import com.example.appweather.ui.components.chart.ChartPoint
import com.example.appweather.ui.components.chart.LineChart
import com.example.appweather.ui.theme.AppWeatherTheme
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel(),
    onOpenLocation: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenMap: () -> Unit,
) {
    val cities by viewModel.cities.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val weatherMap by viewModel.weatherMap.collectAsState()

    if (cities.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val pagerState = rememberPagerState(
        initialPage = cities.indexOf(selectedCity).coerceAtLeast(0),
        pageCount = { cities.size }
    )

    LaunchedEffect(pagerState.currentPage) {
        val city = cities.getOrNull(pagerState.currentPage)
        city?.let { viewModel.selectCity(it) }
    }

    var titleAlpha by remember { mutableFloatStateOf(0f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = selectedCity?.name.orEmpty(),
                        modifier = Modifier.alpha(titleAlpha),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onOpenLocation) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(id = R.string.text_pick_location)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(id = R.string.settings)
                        )
                    }
                    IconButton(onClick = onOpenMap) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = Color.Transparent
    ) { padding ->

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = modifier.fillMaxSize(),
                pageSize = PageSize.Fill
            ) { page ->
                val city = cities[page]
                val uiState = weatherMap[city]

                when {
                    uiState == null || uiState.isLoading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }

                    uiState.errorMessage != null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                uiState.errorMessage,
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    !uiState.isNetworkAvailable -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = stringResource(id = R.string.error_no_network),
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    else -> {
                        WeatherBottomSheetScaffold(
                            city = city,
                            uiState = uiState,
                            onProgressChanged = { rawProgress ->
                                if (selectedCity == city) {
                                    titleAlpha = ((rawProgress - 0.5f) / 0.5f).coerceIn(0f, 1f)
                                }
                            }
                        )
                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeatherBottomSheetScaffold(
    city: City,
    uiState: WeatherUiState,
    onProgressChanged: (Float) -> Unit,
) {
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true
    )
    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)

    val coroutineScope = rememberCoroutineScope()
    var progress by rememberSaveable(city.name) { mutableFloatStateOf(0f) }
    var initialOffset by rememberSaveable(city.name) { mutableFloatStateOf(0f) }

    LaunchedEffect(sheetState) {
        snapshotFlow { runCatching { sheetState.requireOffset() }.getOrNull() }
            .filterNotNull()
            .collect { offset ->
                if (initialOffset == 0f && sheetState.currentValue == SheetValue.PartiallyExpanded) {
                    initialOffset = offset
                }
                if (initialOffset > 0f) {
                    progress = ((initialOffset - offset) / initialOffset).coerceIn(0f, 1f)
                    onProgressChanged(progress)
                }
            }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 200.dp,
        sheetDragHandle = null,
        sheetShadowElevation = 0.dp,
        sheetContainerColor = Color.Transparent,
        sheetShape = RectangleShape,
        sheetContent = {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column {
                        HourlyForecast(uiState.hourlyForecast)

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                        ) {
                            val baseItemWidth = 40.dp
                            val minChartWidth = 640.dp
                            val calculatedWidth = baseItemWidth * uiState.hourlyForecast.size

                            LineChart(
                                data = uiState.hourlyForecast.map { forecast ->
                                    ChartPoint(
                                        label = forecast.time,
                                        value = forecast.temperature
                                    )
                                },
                                lineColor = Color(0xFFFFF100),
                                gradientColor = Color(0xFFFFF100),
                                showAxis = false,
                                showPoints = false,
                                modifier = Modifier
                                    .width(maxOf(calculatedWidth, minChartWidth))
                                    .background(
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            )
                        }
                    }
                }

                val astroItems = uiState.astroInfo?.let { astro ->
                    listOf(
                        Triple(R.string.sunrise, astro.sunrise, R.drawable.ic_sunrise),
                        Triple(R.string.sunset, astro.sunset, R.drawable.ic_sunset),
                        Triple(R.string.moonrise, astro.moonrise, R.drawable.ic_moonrise),
                        Triple(R.string.moonset, astro.moonset, R.drawable.ic_moonset),
                        Triple(R.string.moon_phase, astro.moonPhase, R.drawable.ic_moon_phase),
                    )
                } ?: emptyList()

                astroItems.forEach { (titleRes, value, iconRes) ->
                    item {
                        WeatherDetailCard(
                            title = stringResource(id = titleRes),
                            value = value,
                            iconProgress = painterResource(id = iconRes)
                        )
                    }
                }

                item {
                    WeatherDetailCard(
                        title = stringResource(id = R.string.pressure),
                        value = uiState.pressure.toString(),
                        iconProgress = painterResource(id = R.drawable.ic_arrow_down),
                        labelProgress = "mmHg",
                        showProgress = true,
                    )
                }

                val cards = listOf(
                    Triple(R.string.uv_index, uiState.uvIndex.toString(), R.drawable.ic_uv_index),
                    Triple(R.string.wind, uiState.windDir, R.drawable.ic_wind),
                    Triple(R.string.humidity, "${uiState.humidity}%", R.drawable.ic_humidity),
                    Triple(R.string.cloud, "${uiState.cloud}%", R.drawable.ic_cloud)
                )

                cards.forEach { (title, value, icon) ->
                    item {
                        WeatherDetailCard(
                            title = stringResource(id = title),
                            value = value,
                            iconProgress = painterResource(id = icon),
                            progress = when (title) {
                                R.string.uv_index -> uiState.uvIndex.toFloat() / 10
                                R.string.humidity -> uiState.humidity.toFloat() / 100
                                R.string.cloud -> uiState.cloud.toFloat() / 100
                                else -> 0f
                            }
                        )
                    }
                }
            }
        },
        containerColor = Color.Transparent,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(innerPadding)
                    .graphicsLayer { alpha = 1f - (progress * 1.3f) }
                    .blur(10.dp * progress)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { change, dragAmount ->
                                when {
                                    dragAmount < -20 && sheetState.currentValue == SheetValue.PartiallyExpanded -> {
                                        coroutineScope.launch { sheetState.expand() }
                                    }

                                    dragAmount > 20 && sheetState.currentValue == SheetValue.Expanded -> {
                                        coroutineScope.launch { sheetState.partialExpand() }
                                    }
                                }
                            }
                        )
                    }
                    .padding(horizontal = 16.dp)
            ) {
                WeatherScreenContent(
                    forecastDays = uiState.forecastDays,
                    dateText = uiState.dateText,
                    weekDayText = uiState.weekDayText,
                    cityName = uiState.cityName,
                    temperature = uiState.temperature.toString() + "°",
                    windSpeed = uiState.windSpeed.toInt().toString() + " км/ч",
                    humidity = uiState.humidity.toString() + "%",
                    iconUrl = uiState.iconUrl,
                    conditionText = uiState.condition
                )
            }
        }
    )
}

@Composable
private fun WeatherScreenContent(
    forecastDays: List<ForecastUiItem>,
    cityName: String,
    temperature: String,
    windSpeed: String,
    humidity: String,
    iconUrl: String,
    conditionText: String,
    dateText: String,
    weekDayText: String,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = cityName,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp)
        )

        Text(
            text = buildAnnotatedString {
                val baseStyle = MaterialTheme.typography.titleLarge.toSpanStyle()

                if (dateText.isNotEmpty()) {
                    withStyle(baseStyle.copy(color = MaterialTheme.colorScheme.secondary)) {
                        append(dateText)
                    }
                }

                if (dateText.isNotEmpty() && weekDayText.isNotEmpty()) {
                    withStyle(baseStyle.copy(color = MaterialTheme.colorScheme.secondary)) {
                        append(", ")
                    }
                }

                if (weekDayText.isNotEmpty()) {
                    withStyle(baseStyle.copy(color = MaterialTheme.colorScheme.tertiary)) {
                        append(weekDayText)
                    }
                }
            },
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = temperature,
            fontSize = 80.sp,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = iconUrl,
                contentDescription = conditionText,
                placeholder = painterResource(id = R.drawable.ic_no_image),
                error = painterResource(id = R.drawable.ic_error_picture),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = conditionText,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_wind),
                    contentDescription = stringResource(id = R.string.wind_speed),
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 12.dp),
                )
                Text(
                    text = windSpeed,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Spacer(modifier = Modifier.width(24.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_humidity),
                    contentDescription = stringResource(id = R.string.humidity),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = humidity,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(forecastDays) { item ->
                ForecastCard(item)
            }
        }

    }
}

@Preview
@Composable
private fun PreviewWeatherBottomSheetScaffold() {
    val mockHourlyForecast = listOf(
        HourlyForecastUiItem("12:00", 20, "", "Ясно", 50, 10.0, "NW"),
        HourlyForecastUiItem("13:00", 21, "", "Ясно", 52, 12.0, "NW"),
        HourlyForecastUiItem("14:00", 22, "", "Облачно", 55, 15.0, "N")
    )

    val mockForecastDays = listOf(
        ForecastUiItem("03.09", "", "Ясно", 15, 25, 20),
        ForecastUiItem("04.09", "", "Облачно", 17, 23, 20),
        ForecastUiItem("05.09", "", "Дождь", 16, 22, 19)
    )

    val mockAstroInfo = AstroUiItem(
        sunrise = "06:00",
        sunset = "19:00",
        moonrise = "20:00",
        moonset = "06:00",
        moonPhase = "Full",
        moonIllumination = "100%"
    )

    val mockWeatherUiState = WeatherUiState(
        isLoading = false,
        cityName = "Москва",
        dateText = "3 сентября",
        weekDayText = "Вторник",
        temperature = 21,
        condition = "Ясно",
        iconUrl = "",
        humidity = 65,
        windSpeed = 10.0,
        windDir = "NW",
        uvIndex = 5.0,
        pressure = 750.0,
        cloud = 20,
        forecastDays = mockForecastDays,
        hourlyForecast = mockHourlyForecast,
        astroInfo = mockAstroInfo
    )

    AppWeatherTheme {
        WeatherBottomSheetScaffold(
            city = City(id = 1, name = "Москва", lat = 55.75, lon = 37.61, isDefault = true),
            uiState = mockWeatherUiState,
            onProgressChanged = {}
        )
    }
}
