package com.example.appweather.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
internal fun CircularAnimatedCheckButton(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    size: Dp = 22.dp,
    borderColor: Color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
    checkColor: Color = MaterialTheme.colorScheme.secondary,
) {
    val scaleAnim = remember { Animatable(0f) }
    val pressScale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(checked) {
        if (checked) {
            scaleAnim.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        } else {
            scaleAnim.snapTo(0f)
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .clickable {
                scope.launch {
                    pressScale.animateTo(
                        targetValue = 0.8f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                    pressScale.animateTo(
                        targetValue = 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                    onCheckedChange()
                }
            }
            .scale(pressScale.value),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent, CircleShape)
                .border(2.dp, borderColor, CircleShape)
        )

        if (checked) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = checkColor,
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, checkColor, CircleShape)
                    .scale(scaleAnim.value)
            )
        }
    }
}
