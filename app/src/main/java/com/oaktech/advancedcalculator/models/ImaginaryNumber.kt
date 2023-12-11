package com.oaktech.advancedcalculator.models

import com.oaktech.advancedcalculator.Constants
import com.oaktech.advancedcalculator.extensions.roughSqrt
import com.oaktech.advancedcalculator.extensions.toFinalAnswerPrecision
import com.oaktech.advancedcalculator.extensions.toFinalAnswerPrecisionString
import com.oaktech.advancedcalculator.extensions.toPreciseBigDecimal
import java.math.BigDecimal
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Implementation of an imaginary number used throughout the application
 */
data class ImaginaryNumber(val real: BigDecimal, val imaginary: BigDecimal = BigDecimal.ZERO) {
    /**
     * Finds cubic roots of an imaginary number in polar coordinates
     *
     * This is calculated using the trigonometric method
     *
     * @param magnitude the magnitude of the imaginary number in polar coordinates
     * @param phase the phase angle of the imaginary number in polar coordinates
     *
     * @return the three cube roots
     *
     */
    fun cubeRoots(): MutableList<ImaginaryNumber> {
        val newMagnitude = Math.cbrt(magnitude().toDouble()).toPreciseBigDecimal()
        val phaseShifts = listOf(
            0.toPreciseBigDecimal(),
            2.toPreciseBigDecimal() * Math.PI.toPreciseBigDecimal(),
            -2.toPreciseBigDecimal() * Math.PI.toPreciseBigDecimal()
        )

        val roots = mutableListOf<ImaginaryNumber>()
        for (phaseShift in phaseShifts) {
            val newAngle = (phaseAngle() + phaseShift) / 3.toPreciseBigDecimal()
            roots.add(
                ImaginaryNumber(
                    real = newMagnitude * cos(newAngle.toDouble()).toPreciseBigDecimal(),
                    imaginary = newMagnitude * sin(newAngle.toDouble()).toPreciseBigDecimal()
                )
            )
            roots.sortByDescending { it.real.abs() }
        }
        return roots
    }

    /**
     * Check is element is non zero
     *
     * @return whether [real] or [imaginary] is nonzero
     */
    fun isNonZero(): Boolean {
        return real.stripTrailingZeros().signum() != 0 || imaginary.stripTrailingZeros()
            .signum() != 0
    }

    /**
     * Calculates the multiplicative inverse of the number
     *
     * @return multiplicative inverse
     */
    fun inverse(): ImaginaryNumber {
        val magnitudeSquare = real.pow(2) + imaginary.pow(2)
        return ImaginaryNumber(
            real = real / magnitudeSquare, imaginary = -imaginary / magnitudeSquare
        )
    }

    /**
     * Calculates the vector magnitude of the number
     *
     * @return vector magnitude
     */
    fun magnitude(): BigDecimal {
        return (real.pow(2) + imaginary.pow(2)).roughSqrt()
    }

    /**
     * Calculates the phase angle of the number
     *
     * @return phase angle in radians
     */
    fun phaseAngle(): BigDecimal {
        return when {
            real.stripTrailingZeros() == BigDecimal.ZERO -> 0.5.toPreciseBigDecimal() * Math.PI.toPreciseBigDecimal()
            else -> atan2(imaginary.toDouble(), real.toDouble()).toPreciseBigDecimal()
        }
    }

    override fun toString(): String {
        var output = ""
        if (real.toFinalAnswerPrecision().abs().stripTrailingZeros().signum() != 0) {
            output += real.toFinalAnswerPrecisionString()
        }
        if (imaginary.toFinalAnswerPrecision().abs().stripTrailingZeros().signum() != 0) {
            if (output.length > 0) {
                output += " ${if (imaginary.signum() > 0) Constants.plusSymbol else Constants.minusSymbol} "
                if (imaginary.toFinalAnswerPrecision().abs()
                        .stripTrailingZeros() != BigDecimal.ONE
                ) {
                    output += imaginary.abs().toFinalAnswerPrecisionString()
                }
            } else {
                output += "${if (imaginary.signum() > 0) Constants.plusSymbol else Constants.minusSymbol}"
                if (imaginary.toFinalAnswerPrecision().abs()
                        .stripTrailingZeros() != BigDecimal.ONE
                ) {
                    output += imaginary.abs().toFinalAnswerPrecisionString()
                }
            }
            output += Constants.imaginarySymbol
        }
        return if (output.isNotBlank()) output else "0"
    }

    operator fun plus(other: BigDecimal): ImaginaryNumber {
        return ImaginaryNumber(real = real + other, imaginary = imaginary)
    }

    operator fun plus(other: ImaginaryNumber): ImaginaryNumber {
        return ImaginaryNumber(real = real + other.real, imaginary = imaginary + other.imaginary)
    }

    operator fun minus(other: BigDecimal): ImaginaryNumber {
        return ImaginaryNumber(real = real - other, imaginary = imaginary)
    }

    operator fun div(other: BigDecimal): ImaginaryNumber {
        return ImaginaryNumber(real = real / other, imaginary = imaginary / other)
    }

    operator fun times(other: BigDecimal): ImaginaryNumber {
        return ImaginaryNumber(real = real * other, imaginary = imaginary * other)
    }

    operator fun unaryMinus(): ImaginaryNumber {
        return ImaginaryNumber(real = -real, imaginary = -imaginary)
    }
}