package com.example.calendarcompose.screen.monthview.model

import android.annotation.SuppressLint
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

enum class BorderOrder {
    Start, Center, End, Hole
}


@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.drawSegmentedBorder(
    strokeWidth: Dp,
    borderColor: Color,
    cornerPercent: Int,
    borderOrder: BorderOrder,
    backgroundColor: Color
) = composed(
    factory = {

        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height
            val cornerRadius = width * cornerPercent / 100
            val cornerRadiusBackground = (width - strokeWidthPx) * cornerPercent / 100

            when (borderOrder) {
                BorderOrder.Start -> drawStart(
                    borderColor,
                    width, height,
                    cornerRadius,
                    strokeWidthPx,
                    backgroundColor,
                    cornerRadiusBackground
                )

                BorderOrder.Center -> drawCenter(
                    borderColor,
                    width, height,
                    strokeWidthPx,
                    backgroundColor
                )

                BorderOrder.End -> drawEnd(
                    borderColor,
                    width, height,
                    cornerRadius,
                    strokeWidthPx,
                    backgroundColor,
                    cornerRadiusBackground
                )
                BorderOrder.Hole -> {
                    drawRoundRect(backgroundColor, Offset(0f+4f, 0f), Size(width-8f, height) , CornerRadius(cornerRadius, cornerRadius))
                }
            }
        }
    }
)

private fun DrawScope.drawStart(
    borderColor: Color,
    width: Float, height: Float,
    cornerRadius: Float,
    strokeWidthPx: Float,
    backgroundColor: Color,
    backgroundRadius: Float
) {
    drawLine(
        color = borderColor,
        start = Offset(x = width, y = 0f),
        end = Offset(x = cornerRadius, y = 0f),
        strokeWidth = strokeWidthPx
    )

    // Top left arc
    drawArc(
        color = borderColor,
        startAngle = 180f,
        sweepAngle = 90f,
        useCenter = false,
        topLeft = Offset.Zero,
        size = Size(cornerRadius * 2, cornerRadius * 2),
        style = Stroke(width = strokeWidthPx)
    )
    drawLine(
        color = borderColor,
        start = Offset(x = 0f, y = cornerRadius),
        end = Offset(x = 0f, y = height - cornerRadius),
        strokeWidth = strokeWidthPx
    )
    // Bottom left arc
    drawArc(
        color = borderColor,
        startAngle = 90f,
        sweepAngle = 90f,
        useCenter = false,
        topLeft = Offset(x = 0f, y = height - 2 * cornerRadius),
        size = Size(cornerRadius * 2, cornerRadius * 2),
        style = Stroke(width = strokeWidthPx)
    )
    drawLine(
        color = borderColor,
        start = Offset(x = cornerRadius, y = height),
        end = Offset(x = width, y = height),
        strokeWidth = strokeWidthPx
    )
    backgroundDraw(
        backgroundRadius, 0f + density,
        width, height - strokeWidthPx,
        density,
        density
    ).also {
        drawPath(it, color = backgroundColor)
    }
}

private fun DrawScope.drawEnd(
    borderColor: Color,
    width: Float, height: Float,
    cornerRadius: Float,
    strokeWidthPx: Float,
    backgroundColor: Color,
    backgroundRadius: Float
){
    drawLine(
        color = borderColor,
        start = Offset(x = 0f, y = 0f),
        end = Offset(x = width - cornerRadius, y = 0f),
        strokeWidth = strokeWidthPx
    )

    // Top right arc
    drawArc(
        color = borderColor,
        startAngle = 270f,
        sweepAngle = 90f,
        useCenter = false,
        topLeft = Offset(x = width - cornerRadius * 2, y = 0f),
        size = Size(cornerRadius * 2, cornerRadius * 2),
        style = Stroke(width = strokeWidthPx)
    )
    drawLine(
        color = borderColor,
        start = Offset(x = width, y = cornerRadius),
        end = Offset(x = width, y = height - cornerRadius),
        strokeWidth = strokeWidthPx
    )
    // Bottom right arc
    drawArc(
        color = borderColor,
        startAngle = 0f,
        sweepAngle = 90f,
        useCenter = false,
        topLeft = Offset(
            x = width - 2 * cornerRadius,
            y = height - 2 * cornerRadius
        ),
        size = Size(cornerRadius * 2, cornerRadius * 2),
        style = Stroke(width = strokeWidthPx)
    )
    drawLine(
        color = borderColor,
        start = Offset(x = 0f, y = height),
        end = Offset(x = width - cornerRadius, y = height),
        strokeWidth = strokeWidthPx
    )
    backgroundDraw(
        0f, backgroundRadius,
        width - density, height - strokeWidthPx,
        0f,
        density
    ).also {
        drawPath(it, color = backgroundColor)
    }
}

private fun DrawScope.drawCenter(
    borderColor: Color,
    width: Float, height: Float,
    strokeWidthPx: Float,
    backgroundColor: Color
) {
    drawLine(
        color = borderColor,
        start = Offset(x = 0f, y = 0f),
        end = Offset(x = width, y = 0f),
        strokeWidth = strokeWidthPx
    )
    drawLine(
        color = borderColor,
        start = Offset(x = 0f, y = height),
        end = Offset(x = width, y = height),
        strokeWidth = strokeWidthPx
    )

    backgroundDraw(
        0f, 0f,
        width, height - strokeWidthPx,
        0f, density
    ).also {
        drawPath(it, color = backgroundColor)
    }
}

fun backgroundDraw(
    cornerLeft: Float, cornerRight: Float,
    width: Float, height: Float,
    x: Float, y: Float
): Path {
    val cornerRadiusLeft = CornerRadius(cornerLeft, cornerLeft)
    val cornerRadiusRight = CornerRadius(cornerRight, cornerRight)
    return Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = Offset(x, y),
                    size = Size(width, height),
                ),
                topLeft = cornerRadiusLeft,
                bottomLeft = cornerRadiusLeft,
                topRight = cornerRadiusRight,
                bottomRight = cornerRadiusRight
            )
        )
    }
}