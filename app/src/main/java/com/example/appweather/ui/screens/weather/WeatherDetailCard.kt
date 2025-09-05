package com.example.appweather.ui.screens.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appweather.R
import com.example.appweather.ui.components.CustomArcProgressIndicator
import com.example.appweather.ui.theme.AppWeatherTheme

@Composable
internal fun WeatherDetailCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String = "",
    iconProgress: Painter,
    labelProgress: String = "",
    progress: Float = 0f,
    showProgress: Boolean = false,
    showUvGradientColor: Boolean = false,
    showPressureGradientColor: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                lineHeight = 16.sp,
                fontSize = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        CustomArcProgressIndicator(
            progress = progress,
            text = labelProgress,
            icon = iconProgress,
            color = MaterialTheme.colorScheme.secondary,
            showArcAndText = showProgress,
            isUvIndex = showUvGradientColor,
            isPressure = showPressureGradientColor,
            modifier = Modifier.size(60.dp),
        )

    }
}

@Preview
@Composable
private fun WeatherDetailCardPreview() {
    AppWeatherTheme {
        WeatherDetailCard(
            title = "Влажность",
            value = "65%",
            subtitle = "Комфортно",
            iconProgress = painterResource(id = R.drawable.ic_arrow_down),
            labelProgress = "мбар",
            progress = 0.65f,
            showProgress = true,
        )
    }
}
