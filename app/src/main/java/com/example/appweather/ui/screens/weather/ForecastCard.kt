package com.example.appweather.ui.screens.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.appweather.R
import com.example.appweather.ui.components.CustomHorizontalDivider
import com.example.appweather.ui.theme.AppWeatherTheme

@Composable
internal fun ForecastCard(
    item: ForecastUiItem,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .width(115.dp)
            .height(225.dp)
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
    ) {
        Text(
            text = item.date,
            fontSize = 16.sp,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(4.dp)
        )

        CustomHorizontalDivider()

        AsyncImage(
            modifier = Modifier
                .size(54.dp)
                .padding(top = 4.dp),
            model = item.iconUrl,
            contentDescription = item.condition,
            placeholder = painterResource(id = R.drawable.ic_no_image),
            error = painterResource(id = R.drawable.ic_error_picture)
        )
        Text(
            text = "${item.minTemp}° - ${item.maxTemp}°",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = "${item.avgTemp}°",
            style = MaterialTheme.typography.displayMedium,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = item.condition,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.titleMedium,
            lineHeight = 12.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun ForecastCardPreview() {
    val mockItem = ForecastUiItem(
        date = "28 Aug",
        iconUrl = "",
        condition = "Partly cloudy",
        minTemp = 18,
        maxTemp = 25,
        avgTemp = 21
    )

    AppWeatherTheme {
        ForecastCard(item = mockItem)
    }
}
