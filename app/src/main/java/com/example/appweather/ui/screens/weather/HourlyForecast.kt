package com.example.appweather.ui.screens.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.appweather.R
import com.example.appweather.ui.theme.AppWeatherTheme

@Composable
internal fun HourlyForecast(hourlyData: List<HourlyForecastUiItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp)
    ) {
        Text(
            text = stringResource(id = R.string.forecast_24h),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
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
private fun HourlyForecastItem(hour: HourlyForecastUiItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.width(75.dp)
    ) {
        Text(
            text = hour.time,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        AsyncImage(
            modifier = Modifier.size(36.dp),
            model = hour.iconUrl,
            contentDescription = hour.condition,
            placeholder = painterResource(id = R.drawable.ic_no_image),
            error = painterResource(id = R.drawable.ic_error_picture),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "${hour.temperature}°",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_humidity),
                contentDescription = stringResource(id = R.string.humidity),
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = "${hour.humidity}%",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_wind),
                contentDescription = stringResource(id = R.string.wind_speed),
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .size(16.dp)
                    .padding(end = 4.dp),
            )
            Text(
                text = "${hour.windSpeed.toInt()} км/ч",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun HourlyForecastItemPreview() {
    AppWeatherTheme {
        HourlyForecastItem(
            hour = HourlyForecastUiItem(
                time = "12:00",
                temperature = 23,
                iconUrl = "",
                condition = "Sunny",
                humidity = 65,
                windSpeed = 12.5,
                windDir = "NE"
            )
        )
    }
}

@Preview
@Composable
private fun HourlyForecastSectionPreview() {
    AppWeatherTheme {
        HourlyForecast(
            hourlyData = listOf(
                HourlyForecastUiItem(
                    time = "12:00",
                    temperature = 23,
                    iconUrl = "",
                    condition = "Sunny",
                    humidity = 65,
                    windSpeed = 12.5,
                    windDir = "NE"
                ),
                HourlyForecastUiItem(
                    time = "13:00",
                    temperature = 24,
                    iconUrl = "",
                    condition = "Cloudy",
                    humidity = 60,
                    windSpeed = 10.2,
                    windDir = "E"
                ),
                HourlyForecastUiItem(
                    time = "14:00",
                    temperature = 25,
                    iconUrl = "",
                    condition = "Partly Cloudy",
                    humidity = 55,
                    windSpeed = 8.7,
                    windDir = "SE"
                ),
                HourlyForecastUiItem(
                    time = "15:00",
                    temperature = 26,
                    iconUrl = "",
                    condition = "Sunny",
                    humidity = 50,
                    windSpeed = 7.3,
                    windDir = "S"
                ),
                HourlyForecastUiItem(
                    time = "16:00",
                    temperature = 24,
                    iconUrl = "",
                    condition = "Rain",
                    humidity = 75,
                    windSpeed = 15.1,
                    windDir = "SW"
                )
            )
        )
    }
}
