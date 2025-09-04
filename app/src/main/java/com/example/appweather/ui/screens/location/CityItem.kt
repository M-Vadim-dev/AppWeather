package com.example.appweather.ui.screens.location

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.appweather.R
import com.example.appweather.ui.components.CircularAnimatedCheckButton
import com.example.appweather.ui.theme.AppWeatherTheme
import com.example.appweather.ui.theme.Yellow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
internal fun CustomSwipeBoxCityItem(
    cityName: String,
    temperature: String,
    condition: String,
    localTime: String,
    iconUrl: String,
    isDay: Boolean,
    isDefault: Boolean,
    isEditMode: Boolean,
    pendingDefaultCity: String?,
    onSelectCity: (String) -> Unit,
    onRemoveCity: (String) -> Unit,
    onSelectDefaultCandidate: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val iconWidthPx = with(density) { 48.dp.toPx() }
    val iconPaddingPx = with(density) { 14.dp.toPx() }
    val maxOffsetX = iconWidthPx + iconPaddingPx

    val offsetX = remember(cityName) { Animatable(0f) }

    val fraction = (-offsetX.value / maxOffsetX).coerceIn(0f, 1f)
    val alpha by animateFloatAsState(fraction)
    val scale by animateFloatAsState(0.8f + 0.2f * fraction)

    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { change, dragAmount ->
                        val newOffset = (offsetX.value + dragAmount).coerceIn(-maxOffsetX, 0f)
                        change.consume()
                        scope.launch { offsetX.snapTo(newOffset) }
                    },
                    onDragEnd = {
                        val target = if (offsetX.value > -maxOffsetX / 3) 0f else -maxOffsetX
                        scope.launch { offsetX.animateTo(target, animationSpec = tween(200)) }
                    }
                )
            }
    ) {
        IconButton(
            onClick = { onRemoveCity(cityName) },
            modifier = Modifier
                .padding(end = 4.dp)
                .alpha(alpha)
                .scale(scale),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                Icons.Outlined.Delete,
                contentDescription = stringResource(id = R.string.delete_city),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Box(
            modifier = Modifier.offset { IntOffset(offsetX.value.roundToInt(), 0) }
        ) {
            CityItem(
                cityName = cityName,
                temperature = temperature,
                condition = condition,
                localTime = localTime,
                iconUrl = iconUrl,
                isDay = isDay,
                isDefault = isDefault,
                isEditMode = isEditMode,
                pendingDefaultCity = pendingDefaultCity,
                onSelect = { onSelectCity(cityName) },
                onSelectDefaultCandidate = { onSelectDefaultCandidate(it) },
            )
        }
    }
}

@Composable
internal fun CityItem(
    modifier: Modifier = Modifier,
    cityName: String,
    condition: String,
    temperature: String,
    localTime: String,  //todo
    iconUrl: String,
    isDay: Boolean,
    isDefault: Boolean,
    isEditMode: Boolean,
    pendingDefaultCity: String?,
    onSelect: () -> Unit,
    onSelectDefaultCandidate: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (isDay) MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onSelect),
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = cityName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (isDefault) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_map_marker_star),
                            contentDescription = stringResource(id = R.string.default_city),
                            tint = Yellow,
                            modifier = Modifier
                                .size(22.dp)
                                .padding(start = 4.dp)
                        )
                    }
                }
                Text(
                    text = condition,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

            }
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = temperature,
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            AsyncImage(
                modifier = Modifier.size(38.dp),
                model = iconUrl,
                contentDescription = condition,
                placeholder = ColorPainter(Color.Gray),
                error = painterResource(id = R.drawable.ic_error_picture)
            )

            if (isEditMode) {
                CircularAnimatedCheckButton(
                    modifier = Modifier.padding(start = 12.dp),
                    checkColor = Yellow,
                    checked = cityName == (pendingDefaultCity ?: if (isDefault) cityName else null),
                    onCheckedChange = { onSelectDefaultCandidate(cityName) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun CityItemPreview() {
    AppWeatherTheme {
        CityItem(
            cityName = "Москва",
            temperature = "24°",
            localTime = "2025-08-27",
            isDefault = true,
            isEditMode = false,
            onSelect = {},
            condition = "Ясно",
            iconUrl = "",
            isDay = true,
            pendingDefaultCity = null,
            onSelectDefaultCandidate = {}
        )
    }
}

@Preview
@Composable
private fun PreviewSwipeBoxCityItem() {
    AppWeatherTheme {
        CustomSwipeBoxCityItem(
            cityName = "Moscow",
            temperature = "20°",
            condition = "Sunny",
            localTime = "12:00",
            iconUrl = "",
            isDay = true,
            isDefault = true,
            isEditMode = true,
            pendingDefaultCity = null,
            onSelectCity = {},
            onRemoveCity = {},
            onSelectDefaultCandidate = {}
        )
    }
}