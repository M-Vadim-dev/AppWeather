package com.example.appweather.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.appweather.R
import com.example.appweather.ui.theme.AppWeatherTheme

@Composable
internal fun CustomArcProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    text: String,
    icon: Painter,
    iconColor: Color = MaterialTheme.colorScheme.tertiary,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    strokeWidth: Dp = 5.dp,
    totalSweepAngle: Float = 265f,
    startAngle: Float = 90f - totalSweepAngle - 90f / 2f,
    showArcAndText: Boolean = true,
    isUvIndex: Boolean = false,
    isPressure: Boolean = false,
) {
    val arcColor = when {
        isUvIndex -> getUvGradientColor(progress)
        isPressure -> getPressureGradientColor(progress)
        else -> color
    }

    Box(
        modifier = modifier.size(80.dp),
        contentAlignment = Alignment.Center
    ) {
        if (showArcAndText) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val sweepAngle = totalSweepAngle * progress
                val strokeWidthPx = strokeWidth.toPx()
                val canvasSize = size.minDimension - strokeWidthPx

                drawArc(
                    color = trackColor,
                    startAngle = startAngle,
                    sweepAngle = totalSweepAngle,
                    useCenter = false,
                    style = Stroke(strokeWidthPx),
                    size = Size(canvasSize, canvasSize),
                    topLeft = Offset(
                        (size.width - canvasSize) / 2,
                        (size.height - canvasSize) / 2
                    )
                )

                drawArc(
                    color = arcColor,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(strokeWidthPx, cap = StrokeCap.Round),
                    size = Size(canvasSize, canvasSize),
                    topLeft = Offset(
                        (size.width - canvasSize) / 2,
                        (size.height - canvasSize) / 2
                    )
                )
            }
        }

        Icon(
            painter = icon,
            contentDescription = stringResource(id = R.string.wind),
            tint = iconColor,
            modifier = Modifier.size(32.dp),
        )

        if (showArcAndText) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.offset(y = 24.dp),
            )
        }

    }
}

@Composable
private fun getUvGradientColor(progress: Float): Color {
    val uvValue = progress * 11f

    return when {
        uvValue < 0.01f -> Color(0xFF4CAF50)
        uvValue < 2.5f -> Color(0xFF8BC34A)
        uvValue < 5.5f -> Color(0xFFFFEB3B)
        uvValue < 7.5f -> Color(0xFFFF9800)
        uvValue < 10.5f -> Color(0xFFF44336)
        else -> Color(0xFF9C27B0)
    }
}

@Composable
private fun getPressureGradientColor(progress: Float): Color = when {
    progress < 0.4 -> Color(0xFF2196F3)
    progress < 0.6 -> Color(0xFF4CAF50)
    else -> Color(0xFFF44336)
}

@Preview
@Composable
private fun CustomArcProgressIndicatorPreview() {
    AppWeatherTheme {
        CustomArcProgressIndicator(
            icon = painterResource(id = R.drawable.ic_arrow_down),
            progress = 0.75f,
            text = "mbar"
        )
    }
}
