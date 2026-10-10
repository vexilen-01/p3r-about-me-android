package com.example.p3rprofile.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.p3rprofile.ui.theme.P3RColors
import kotlin.math.cos
import kotlin.math.sin

data class TraitStat(val label: String, val value: Float)

@Composable
fun PentagonStatChart(
    stats: List<TraitStat>,
    modifier: Modifier = Modifier
) {
    require(stats.size == 5) { "Pentagon chart requires exactly 5 stats." }

    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = androidx.compose.animation.core.FastOutSlowInEasing)
        )
    }

    Box(modifier = modifier.size(240.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize().padding(28.dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2f
            val angleStep = (2 * Math.PI / 5).toFloat()
            val startAngle = (-Math.PI / 2).toFloat()

            // Concentric pentagons
            for (level in 1..4) {
                val levelRadius = radius * (level / 4f)
                val gridPath = Path()
                for (i in 0 until 5) {
                    val angle = startAngle + i * angleStep
                    val x = center.x + levelRadius * cos(angle)
                    val y = center.y + levelRadius * sin(angle)
                    if (i == 0) gridPath.moveTo(x, y) else gridPath.lineTo(x, y)
                }
                gridPath.close()
                drawPath(
                    path = gridPath,
                    color = P3RColors.OceanBlue.copy(alpha = 0.35f),
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }

            // Radiating axes
            for (i in 0 until 5) {
                val angle = startAngle + i * angleStep
                val x = center.x + radius * cos(angle)
                val y = center.y + radius * sin(angle)
                drawLine(
                    color = P3RColors.ElectricCyan.copy(alpha = 0.4f),
                    start = center,
                    end = Offset(x, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Data Polygon
            val dataPath = Path()
            for (i in 0 until 5) {
                val angle = startAngle + i * angleStep
                val statVal = stats[i].value * animatedProgress.value
                val x = center.x + (radius * statVal) * cos(angle)
                val y = center.y + (radius * statVal) * sin(angle)
                if (i == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
            }
            dataPath.close()

            drawPath(
                path = dataPath,
                brush = Brush.radialGradient(
                    colors = listOf(
                        P3RColors.ElectricCyan.copy(alpha = 0.7f),
                        P3RColors.OceanBlue.copy(alpha = 0.4f)
                    ),
                    center = center,
                    radius = radius
                )
            )

            drawPath(
                path = dataPath,
                color = P3RColors.PersonaGold,
                style = Stroke(width = 2.5.dp.toPx())
            )
        }
    }
}