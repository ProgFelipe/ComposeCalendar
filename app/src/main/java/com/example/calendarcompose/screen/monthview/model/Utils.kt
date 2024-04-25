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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

enum class BorderOrder {
    Start, Center, End, HOLE
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.drawSegmentedBorder(
    strokeWidth: Dp,
    color: Color,
    cornerPercent: Int,
    borderOrder: BorderOrder,
    drawDivider: Boolean = false
) = composed(
    factory = {

        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height
            val cornerRadius = width * cornerPercent / 100

            when (borderOrder) {
                BorderOrder.Start -> {

                    drawLine(
                        color = color,
                        start = Offset(x = width, y = 0f),
                        end = Offset(x = cornerRadius, y = 0f),
                        strokeWidth = strokeWidthPx
                    )

                    // Top left arc
                    drawArc(
                        color = color,
                        startAngle = 180f,
                        sweepAngle = 90f,
                        useCenter = false,
                        topLeft = Offset.Zero,
                        size = Size(cornerRadius * 2, cornerRadius * 2),
                        style = Stroke(width = strokeWidthPx)
                    )
                    drawLine(
                        color = color,
                        start = Offset(x = 0f, y = cornerRadius),
                        end = Offset(x = 0f, y = height - cornerRadius),
                        strokeWidth = strokeWidthPx
                    )
                    // Bottom left arc
                    drawArc(
                        color = color,
                        startAngle = 90f,
                        sweepAngle = 90f,
                        useCenter = false,
                        topLeft = Offset(x = 0f, y = height - 2 * cornerRadius),
                        size = Size(cornerRadius * 2, cornerRadius * 2),
                        style = Stroke(width = strokeWidthPx)
                    )
                    drawLine(
                        color = color,
                        start = Offset(x = cornerRadius, y = height),
                        end = Offset(x = width, y = height),
                        strokeWidth = strokeWidthPx
                    )

                    /*backgroundDraw(cornerRadius, 0f + density.density, width, height - strokeWidthPx, density.density).also {
                        drawPath(it, color = Color.Black)
                    }*/
                }

                BorderOrder.Center -> {
                    drawLine(
                        color = color,
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = width, y = 0f),
                        strokeWidth = strokeWidthPx
                    )
                    drawLine(
                        color = color,
                        start = Offset(x = 0f, y = height),
                        end = Offset(x = width, y = height),
                        strokeWidth = strokeWidthPx
                    )

                    if (drawDivider) {
                        drawLine(
                            color = color,
                            start = Offset(x = 0f, y = 0f),
                            end = Offset(x = 0f, y = height),
                            strokeWidth = strokeWidthPx
                        )
                    }
                    backgroundDraw(0f, 0f, width, height - strokeWidthPx, density.density).also {
                        drawPath(it, color = Color.DarkGray)
                    }
                }

                else -> {

                    if (drawDivider) {
                        drawLine(
                            color = color,
                            start = Offset(x = 0f, y = 0f),
                            end = Offset(x = 0f, y = height),
                            strokeWidth = strokeWidthPx
                        )
                    }

                    drawLine(
                        color = color,
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = width - cornerRadius, y = 0f),
                        strokeWidth = strokeWidthPx
                    )

                    // Top right arc
                    drawArc(
                        color = color,
                        startAngle = 270f,
                        sweepAngle = 90f,
                        useCenter = false,
                        topLeft = Offset(x = width - cornerRadius * 2, y = 0f),
                        size = Size(cornerRadius * 2, cornerRadius * 2),
                        style = Stroke(width = strokeWidthPx)
                    )
                    drawLine(
                        color = color,
                        start = Offset(x = width, y = cornerRadius),
                        end = Offset(x = width, y = height - cornerRadius),
                        strokeWidth = strokeWidthPx
                    )
                    // Bottom right arc
                    drawArc(
                        color = color,
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
                        color = color,
                        start = Offset(x = 0f, y = height),
                        end = Offset(x = width - cornerRadius, y = height),
                        strokeWidth = strokeWidthPx
                    )

                    backgroundDraw(0f, cornerRadius, width, height - strokeWidthPx, density.density).also {
                        drawPath(it, color = Color.Cyan)
                    }
                }
            }
        }
    }
)

fun backgroundDraw(cornerLeft: Float, cornerRight: Float, x: Float, y: Float, density: Float): Path {

    val cornerRadiusLeft = CornerRadius(cornerLeft, cornerLeft)
    val cornerRadiusRight = CornerRadius(cornerRight, cornerRight)
    return Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = Offset(x = 0f + density, y = density),
                    size = Size(x, y),
                ),
                topLeft = cornerRadiusLeft,
                bottomLeft = cornerRadiusLeft,
                topRight = cornerRadiusRight,
                bottomRight = cornerRadiusRight
            )
        )
    }
}