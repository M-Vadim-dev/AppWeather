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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.appweather.R

@Composable
internal fun CustomArcProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    text: String,
    icon: Painter,
    iconColor: Color = MaterialTheme.colorScheme.secondary,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    strokeWidth: Dp = 5.dp,
    totalSweepAngle: Float = 265f,
    startAngle: Float = 90f - totalSweepAngle - 90f / 2f,
    showArcAndText: Boolean = true,
) {
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
                    color = color,
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
