package com.applicassion.ChatbotTVCompose.ui.widgets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TvCircularProgressIndicator(
    modifier: Modifier = Modifier,
    indicatorSize: Dp = 72.dp,   // <- renamed
    strokeWidth: Dp = 6.dp,
    color: Color = Color.White,
    trackColor: Color = Color.White.copy(alpha = 0.3f)
) {
    val transition = rememberInfiniteTransition(label = "tv_loader")
    val startAngle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "tv_loader_angle"
    )

    Canvas(modifier = modifier.size(indicatorSize)) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)

        // THIS `size` is DrawScope.size (Size), not the Dp param
        val diameter = size.minDimension - stroke.width
        val topLeft = (size.minDimension - diameter) / 2f
        val arcSize = Size(diameter, diameter)
        val topLeftOffset = Offset(topLeft, topLeft)

        // background ring
        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeftOffset,
            size = arcSize,
            style = stroke
        )

        // moving arc segment
        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = topLeftOffset,
            size = arcSize,
            style = stroke
        )
    }
}
