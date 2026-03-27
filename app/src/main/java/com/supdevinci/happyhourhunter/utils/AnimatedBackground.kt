package com.supdevinci.happyhourhunter.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.supdevinci.happyhourhunter.ui.theme.AccentBlue
import com.supdevinci.happyhourhunter.ui.theme.BackgroundCream
import com.supdevinci.happyhourhunter.ui.theme.ErrorRed
import com.supdevinci.happyhourhunter.ui.theme.TagBackground

@Composable
fun AnimatedBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "animated_background")

    val blob1X = transition.animateFloat(
        initialValue = 100f,
        targetValue = 1100f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob1X"
    )

    val blob1Y = transition.animateFloat(
        initialValue = 150f,
        targetValue = 1300f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 13000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob1Y"
    )

    val blob2X = transition.animateFloat(
        initialValue = 1000f,
        targetValue = 80f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob2X"
    )

    val blob2Y = transition.animateFloat(
        initialValue = 250f,
        targetValue = 1500f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob2Y"
    )

    val blob3X = transition.animateFloat(
        initialValue = 500f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob3X"
    )

    val blob3Y = transition.animateFloat(
        initialValue = 1600f,
        targetValue = 250f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 11000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob3Y"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        BackgroundCream,
                        Color(0xFF120A22),
                        BackgroundCream
                    ),
                    start = Offset.Zero,
                    end = Offset(1200f, 2200f)
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            AccentBlue.copy(alpha = 0.40f),
                            Color.Transparent
                        ),
                        center = Offset(blob1X.value, blob1Y.value),
                        radius = 900f
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            ErrorRed.copy(alpha = 0.32f),
                            Color.Transparent
                        ),
                        center = Offset(blob2X.value, blob2Y.value),
                        radius = 850f
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            TagBackground.copy(alpha = 0.28f),
                            Color.Transparent
                        ),
                        center = Offset(blob3X.value, blob3Y.value),
                        radius = 950f
                    )
                )
        )

        content()
    }
}
