package com.example.appweather.ui.components.chart

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appweather.ui.theme.AppWeatherTheme

@Composable
internal fun LineChart(
    modifier: Modifier = Modifier,
    data: List<ChartPoint>,
    showEvery: Int = 1,
    showAxis: Boolean = true,
    showAxisLabels: Boolean = true,
    showValues: Boolean = true,
    showPoints: Boolean = true,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    pointsColor: Color = MaterialTheme.colorScheme.primary,
    gradientColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    axisColor: Color = MaterialTheme.colorScheme.secondary,
) {
    if (data.isEmpty()) return

    val values = data.map { it.value }
    val maxValue = values.maxOrNull() ?: 0
    val minValue = values.minOrNull() ?: 0

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 24.dp)
            .height(80.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val padding = 16.dp.toPx()
            val chartHeight = height - 2 * padding

            val points = data.mapIndexed { index, point ->
                val x = padding + (width - 2 * padding) * (index.toFloat() / (data.size - 1))
                val y = if (maxValue != minValue) {
                    padding + chartHeight * (1 - (point.value - minValue).toFloat() / (maxValue - minValue))
                } else {
                    height / 2
                }
                Offset(x, y)
            }

            if (showAxis) {
                drawLine(
                    color = axisColor.copy(alpha = 0.2f),
                    start = Offset(padding, height - padding),
                    end = Offset(width - padding, height - padding),
                    strokeWidth = 1.dp.toPx()
                )
            }

            if (points.size >= 2) {
                val path = Path().apply {
                    moveTo(points.first().x, points.first().y)

                    for (i in 1 until points.size) {
                        val prev = points[i - 1]
                        val cur = points[i]

                        val control1 = Offset(
                            prev.x + (cur.x - prev.x) * 0.3f,
                            prev.y
                        )
                        val control2 = Offset(
                            cur.x - (cur.x - prev.x) * 0.3f,
                            cur.y
                        )

                        cubicTo(control1.x, control1.y, control2.x, control2.y, cur.x, cur.y)
                    }
                }

                val fillPath = Path().apply {
                    addPath(path)
                    lineTo(points.last().x, height - padding)
                    lineTo(points.first().x, height - padding)
                    close()
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(gradientColor.copy(alpha = 0.3f), Color.Transparent),
                        startY = points.minOf { it.y },
                        endY = height - padding
                    ),
                    style = Fill
                )

                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            points.forEachIndexed { index, point ->
                if (showPoints) {
                    drawCircle(
                        color = pointsColor,
                        radius = 3.dp.toPx(),
                        center = point
                    )
                }

                if (showValues) {
                    drawIntoCanvas { canvas ->
                        canvas.nativeCanvas.drawText(
                            "${data[index].value}",
                            point.x,
                            point.y - 10.dp.toPx(),
                            Paint().apply {
                                color = textColor.toArgb()
                                textSize = 12.sp.toPx()
                                textAlign = Paint.Align.CENTER
                                isAntiAlias = true
                            }
                        )
                    }
                }

                if (showAxisLabels && index % showEvery == 0) {
                    drawIntoCanvas { canvas ->
                        canvas.nativeCanvas.drawText(
                            data[index].label,
                            point.x,
                            height - 5.dp.toPx(),
                            Paint().apply {
                                color = axisColor.toArgb()
                                textSize = 8.sp.toPx()
                                textAlign = Paint.Align.CENTER
                                isAntiAlias = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LineChartPreview() {
    AppWeatherTheme {
        LineChart(
            data = listOf(
                ChartPoint("00:00", 21),
                ChartPoint("01:00", 22),
                ChartPoint("02:00", 23),
                ChartPoint("03:00", 25),
                ChartPoint("04:00", 26),
                ChartPoint("05:00", 28),
                ChartPoint("06:00", 24),
                ChartPoint("07:00", 22),
                ChartPoint("08:00", 18),
                ChartPoint("09:00", 14),
                ChartPoint("10:00", 19)
            ),
            showPoints = false,
        )
    }
}
