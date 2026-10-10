package com.example.p3rprofile.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.tan

fun Modifier.p3rSkew(angleDegrees: Float = -8f): Modifier = this.graphicsLayer {
    val shearX = tan(Math.toRadians(angleDegrees.toDouble())).toFloat()
    val matrix = androidx.compose.ui.graphics.Matrix()
    matrix.values[androidx.compose.ui.graphics.Matrix.SkewX] = -shearX
    transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 0.5f)
}

class AngledBannerShape(
    private val cutAngleDegrees: Float = 10f,
    private val cutLeft: Boolean = true,
    private val cutRight: Boolean = true
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val offset = (size.height * tan(Math.toRadians(cutAngleDegrees.toDouble()))).toFloat()

        val topLeftX = if (cutLeft) offset else 0f
        val bottomLeftX = 0f
        val topRightX = size.width
        val bottomRightX = if (cutRight) size.width - offset else size.width

        path.moveTo(topLeftX, 0f)
        path.lineTo(topRightX, 0f)
        path.lineTo(bottomRightX, size.height)
        path.lineTo(bottomLeftX, size.height)
        path.close()

        return Outline.Generic(path)
    }
}