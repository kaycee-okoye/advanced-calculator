package com.oaktech.advancedcalculator.composable_functions

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.oaktech.advancedcalculator.extensions.toFinalAnswerPrecisionString
import com.oaktech.advancedcalculator.extensions.toPreciseBigDecimal
import com.oaktech.advancedcalculator.ui.theme.AccentColor
import com.oaktech.advancedcalculator.ui.theme.MainColor
import com.oaktech.advancedcalculator.ui.theme.TextColor
import java.math.BigDecimal
import kotlin.math.absoluteValue

/**
 * Element that displays scatter line graph y = f(x)
 *
 * @param x values of the independent variable to plot
 * @param y corresponding values of the dependent variable to plot
 */
@Composable
fun LineGraph(x: List<BigDecimal>, y: List<BigDecimal?>) {
    Canvas(modifier = Modifier.fillMaxSize().padding(10.dp).background(MainColor)) {
        // define dimensions
        val canvasWidth = this.size.width.toDouble().toPreciseBigDecimal()
        val canvasHeight = this.size.height.toDouble().toPreciseBigDecimal()
        val strokeWidth = (0.005f * canvasWidth.toFloat())
        val fontSize = (0.02f * canvasWidth.toFloat())
        val paint = Paint().apply {
            textAlign = Paint.Align.LEFT
            textSize = fontSize
            color = TextColor.toArgb()
        }

        // find extrema
        var minX: BigDecimal? = null
        var maxX: BigDecimal? = null
        var minY: BigDecimal? = null
        var maxY: BigDecimal? = null
        for (i in x.indices) {
            if (y[i] != null) {
                if (minX == null || x[i] < minX) {
                    minX = x[i]
                }
                if (maxX == null || x[i] > maxX) {
                    maxX = x[i]
                }
                if (minY == null || y[i]!! < minY) {
                    minY = y[i]
                }
                if (maxY == null || y[i]!! > maxY) {
                    maxY = y[i]
                }
            }
        }

        if (minX == null) {
            minX = BigDecimal.ONE.negate()
            maxX = BigDecimal.ONE
        }
        if (minY == null) {
            minY = BigDecimal.ONE.negate()
            maxY = BigDecimal.ONE
        }

        if (minX == maxX) {
            if (minX!!.stripTrailingZeros() == BigDecimal.ZERO) {
                minX = BigDecimal.ONE.negate()
                maxX = BigDecimal.ONE
            } else if (minX.signum() <= 0) {
                maxX = -0.01.toPreciseBigDecimal() * minX
            } else if (maxX!!.signum() > 0) {
                minX = -0.01.toPreciseBigDecimal() * maxX
            }
        }
        if (minY == maxY) {
            if (minY!!.stripTrailingZeros() == BigDecimal.ZERO) {
                minY = BigDecimal.ONE.negate()
                maxY = BigDecimal.ONE
            } else if (minY.signum() <= 0) {
                maxY = -0.01.toPreciseBigDecimal() * minY
            } else if (maxY!!.signum() > 0) {
                minY = -0.01.toPreciseBigDecimal() * maxY
            }
        }

        val width = maxX!! - minX!!
        val height = maxY!! - minY!!

        val xScale = (canvasWidth / width).toDouble()
        val yScale = (canvasHeight / height).toDouble()

        val referenceX = minX.toDouble()
        val referenceY = maxY.toDouble()

        val xaxisY = when {
            minY.stripTrailingZeros().signum() > 0 && maxY.stripTrailingZeros()
                .signum() > 0 -> canvasHeight.toFloat()

            minY.stripTrailingZeros().signum() < 0 && maxY.stripTrailingZeros()
                .signum() < 0 -> fontSize * 2

            else -> (((0.0 - referenceY).absoluteValue) * yScale).toFloat()
        }
        val yaxisX = when {
            minX.stripTrailingZeros().signum() > 0 && maxX.stripTrailingZeros()
                .signum() > 0 -> 0f

            minX.stripTrailingZeros().signum() < 0 && maxX.stripTrailingZeros()
                .signum() < 0 -> 0f

            else -> (((0.0 - referenceX).absoluteValue) * xScale).toFloat()
        }

        // draw axes lines
        drawLine(
            color = TextColor,
            start = Offset(x = 0f, y = xaxisY),
            end = Offset(
                x = canvasWidth.toFloat(),
                y = xaxisY
            ),
            strokeWidth = strokeWidth,
        ) // x-axis
        drawLine(
            color = TextColor,
            start = Offset(x = yaxisX, y = 0f),
            end = Offset(
                x = yaxisX,
                y = canvasHeight.toFloat()
            ),
            strokeWidth = strokeWidth,
        ) // y-axis

        // axis numbers
        val axisNumberCount = 7
        val xaxisNumberStep =
            (maxX - minX) / (axisNumberCount + 1).toPreciseBigDecimal()
        val yaxisNumberStep =
            (maxY - minY) / (axisNumberCount + 1).toPreciseBigDecimal()
        for (i in 0..axisNumberCount) {
            val xValue = minX + (i.toPreciseBigDecimal() * xaxisNumberStep)
            if (xValue.stripTrailingZeros() != BigDecimal.ZERO) {
                drawContext.canvas.nativeCanvas.drawText(
                    xValue.toFinalAnswerPrecisionString(),
                    (((xValue.toDouble() - referenceX).absoluteValue) * xScale).toFloat(),
                    xaxisY - fontSize,
                    paint
                )
            }

            val yValue = minY + (i.toPreciseBigDecimal() * yaxisNumberStep)
            if (yValue.stripTrailingZeros() != BigDecimal.ZERO) {
                drawContext.canvas.nativeCanvas.drawText(
                    yValue.toFinalAnswerPrecisionString(),
                    yaxisX + fontSize,
                    (((yValue.toDouble() - referenceY).absoluteValue) * yScale).toFloat(),
                    paint
                )
            }
        }

        // plot graph
        var previousX: BigDecimal? = null
        var previousY: BigDecimal? = null
        for (i in x.indices) {
            if (y[i] != null) {
                if (previousY != null) {
                    // draw line
                    drawLine(
                        color = AccentColor,
                        start = Offset(
                            x = (((previousX!!.toDouble() - referenceX).absoluteValue) * xScale).toFloat(),
                            y = (((previousY.toDouble() - referenceY).absoluteValue) * yScale).toFloat()
                        ),
                        end = Offset(
                            x = (((x[i].toDouble() - referenceX).absoluteValue) * xScale).toFloat(),
                            y = (((y[i]!!.toDouble() - referenceY).absoluteValue) * yScale).toFloat()
                        ),
                        strokeWidth = strokeWidth
                    )
                } else {
                    // draw scatter point
                    drawPoints(
                        listOf(
                            Offset(
                                x = (((x[i].toDouble() - referenceX).absoluteValue) * xScale).toFloat(),
                                y = (((y[i]!!.toDouble() - referenceY).absoluteValue) * yScale).toFloat()
                            )
                        ),
                        PointMode.Points,
                        AccentColor
                    )
                }
            }
            previousX = x[i]
            previousY = y[i]
        }
    }
}