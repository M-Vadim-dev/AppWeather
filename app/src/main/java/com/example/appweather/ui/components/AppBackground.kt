package com.example.appweather.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.imageLoader
import com.example.appweather.R
import com.example.appweather.ui.theme.DarkBlue
import com.example.appweather.ui.theme.DarkSlateBlue
import com.example.appweather.ui.theme.SlateBlue

@Composable
internal fun AppBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(DarkSlateBlue, DarkBlue),
                        startY = 0f,
                        endY = size.height
                    )
                )
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(SlateBlue.copy(alpha = 0.5f), Color.Transparent),
                        center = Offset(x = size.width * 0.4f, y = size.height * 0.2f),
                        radius = size.minDimension * 0.6f
                    )
                )
            }
    ) {

        AsyncImage(
            model = R.raw.sky_with_clouds_and_stars,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = 0.2f }
                .drawWithContent {
                    with(drawContext.canvas) {
                        saveLayer(
                            bounds = Rect(0f, 0f, size.width, size.height),
                            paint = Paint().apply {
                                blendMode = blendMode
                            }
                        )
                        drawContent()
                        restore()
                    }
                },
            contentScale = ContentScale.Crop,
            imageLoader = LocalPlatformContext.current.imageLoader
        )

        content()
    }
}
