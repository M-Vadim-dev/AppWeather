package com.example.appweather.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.appweather.R
import com.example.appweather.domain.model.AstroInfo
import com.example.appweather.domain.model.ForecastItem
import com.example.appweather.domain.model.HourInfo
import com.example.appweather.domain.model.WeatherInfo
import com.example.appweather.ui.components.SearchAndGpsBar
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    Log.d("!!!", state.toString())
    val cities by viewModel.cities.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearchVisible by viewModel.isSearchVisible.collectAsState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            SearchAndGpsBar(
                searchQuery = searchQuery,
                isSearchVisible = isSearchVisible,
                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                onSearch = viewModel::searchCity,
                onUseGps = viewModel::useCurrentLocation,
                onToggleSearchVisibility = viewModel::toggleSearchVisibility,
            )

//            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {  //todo
//                items(cities) { city ->
//                    Button(
//                        onClick = { viewModel.selectCity(city) },
//                        colors = if (city == selectedCity)
//                            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
//                        else ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
//                    ) {
//                        Text(
//                            text = if (city.length > 15) "${city.take(12)}..." else city,
//                            fontSize = 14.sp,
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis
//                        )
//                    }
//                }
//            }

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    state.errorMessage != null -> {
                        Text(
                            text = state.errorMessage ?: "Error",
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }

                    state.weatherInfo != null -> {
                        state.weatherInfo?.let { weatherInfo ->
                            WeatherBottomSheetScaffold(
                                weather = weatherInfo,
                                state = state,
                            )
                        }
                    }

                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherBottomSheetScaffold(
    weather: WeatherInfo,
    state: WeatherState,
) {
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true
    )
    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)

    val coroutineScope = rememberCoroutineScope()

    var progress by remember { mutableFloatStateOf(0f) }
    var initialOffset by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(sheetState) {
        snapshotFlow { sheetState.requireOffset() }
            .filterNotNull()
            .collect { offset ->
                if (initialOffset == 0f && sheetState.currentValue == SheetValue.PartiallyExpanded) {
                    initialOffset = offset
                }
                progress = ((initialOffset - offset) / initialOffset).coerceIn(0f, 1f)
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .verticalScroll(rememberScrollState())
                    .heightIn(min = 180.dp)
            ) {
                HourlyForecastSection(state.hourlyForecast)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "uv = ${weather.uvIndex}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_wind),
                        contentDescription = "Ветер",
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${weather.windKph.toInt()}км/ч",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Направление ветра: ${weather.windDir}",
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {

                state.astroInfo?.let { astro ->
                    AstroInfoSection(astro)
                }
            }
        },
        containerColor = Color.Transparent,
        content = { innerPadding ->

            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(innerPadding)
                    .graphicsLayer {
                        alpha = 1f - (progress * 1.3f)
                    }
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
                state.weatherInfo?.let {
                    WeatherScreenContent(
                        weather = it,
                        dateText = state.dateText,
                        weekDayText = state.weekDayText
                    )
                }
            }

        }
    )
}

@Composable
private fun WeatherScreenContent(
    weather: WeatherInfo,
    dateText: String = "",
    weekDayText: String = "",
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = weather.city,
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
            text = "${weather.temperatureC.toInt()}°",
            fontSize = 80.sp,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = weather.iconUrl,
                contentDescription = weather.condition,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = weather.condition,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 16.sp,
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
                    contentDescription = "Скорость ветра",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 12.dp),
                )
                Text(
                    text = "${weather.windKph} км/ч",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Spacer(modifier = Modifier.width(24.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_humidity),
                    contentDescription = "Влажность",
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "${weather.humidity}%",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }


        }

        Spacer(modifier = Modifier.height(30.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(weather.forecast) { item ->
                ForecastCard(item)
            }
        }

    }
}

@Composable
private fun ForecastCard(item: ForecastItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(120.dp)
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
    ) {
        Text(
            text = item.date,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        AsyncImage(
            model = item.iconUrl,
            contentDescription = item.condition,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = "Дневная температура",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "${item.minTempC.toInt()}° - ${item.maxTempC.toInt()}°",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = "${item.avgTempC.toInt()}°C",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = item.condition,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.tertiary,
            lineHeight = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Composable
private fun HourlyForecastSection(hourlyData: List<HourInfo>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Прогноз на 24 ч",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.height(120.dp)) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(hourlyData) { hour ->
                    HourlyForecastItem(hour)
                }
            }
        }
    }
}

@Composable
private fun WeatherDetail(
    label: String,
    value: String,
    icon: ImageVector? = null,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )


    }
}

@Composable
private fun HourlyForecastItem(hour: HourInfo) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ) {
        Text(
            text = hour.time.split(" ").last(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.tertiary,
            fontWeight = FontWeight.Medium
        )

        AsyncImage(
            model = hour.iconUrl,
            contentDescription = hour.condition,
            modifier = Modifier.size(36.dp)
        )

        Text(
            text = "${hour.tempC.toInt()}°",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${hour.humidity}%",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
            )

            if (hour.chanceOfRain > 0) {
                Text(
                    text = "☔${hour.chanceOfRain}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }

            Text(
                text = "${hour.windKph.toInt()}км/ч",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
private fun AstroInfoSection(
    astro: AstroInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Астрономические данные",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),

            ) {

            WeatherDetail(
                label = "Восход",
                value = astro.sunrise,
            )

            WeatherDetail(
                label = "Закат",
                value = astro.sunset,
            )

            WeatherDetail(
                label = "Восход луны",
                value = astro.moonrise,
            )

            WeatherDetail(
                label = "Закат луны",
                value = astro.moonset,
            )

            WeatherDetail(
                label = "Фаза луны",
                value = astro.moonPhase,
            )

            WeatherDetail(
                label = "Освещенность",
                value = "${astro.moonIllumination}%",
            )

        }
    }
}
