package com.oaktech.advancedcalculator.controllers

import com.oaktech.advancedcalculator.models.ImaginaryNumber
import com.oaktech.advancedcalculator.extensions.roughSqrt
import com.oaktech.advancedcalculator.extensions.toBigDecimalWithSamePrecision
import com.oaktech.advancedcalculator.extensions.toPreciseBigDecimal
import java.math.BigDecimal

/**
 * Contains implementations of algorithms used in calculating polynomial roots
 */
object PolynomialRootCalculations {
    /**
     * Calculates the roots of a polynomial
     *
     * @param coeffs the coefficients of the polynomial. [constants](2)
     * corresponds to the coefficient of x^2.
     * @param order the order of the polynomial and the number of roots
     * to find
     */
    fun findRoots(coeffs: List<String>, order: Int): List<ImaginaryNumber?> {
        if (order == 1) {
            return rootOfLinearEquation(order0 = coeffs[0], order1 = coeffs[1])
        } else if (order == 2) {
            return rootOfQuadraticEquation(
                order0 = coeffs[0],
                order1 = coeffs[1],
                order2 = coeffs[2]
            )
        } else {
            return rootOfCubicEquation(
                order0 = coeffs[0],
                order1 = coeffs[1],
                order2 = coeffs[2],
                order3 = coeffs[3]
            )
        }
    }

    /**
     * Finds the roots of a linear equation
     *
     * @param order0 the constant in the linear equation
     * @param order1 the coefficient of the x term
     *
     * @return the root of the equation, null if the root can't
     * be found
     */
    fun rootOfLinearEquation(order0: String, order1: String): List<ImaginaryNumber?> {
        if (order1.toBigDecimalWithSamePrecision() == BigDecimal.ZERO) {
            return listOf(null)
        }

        val m = order1.toPreciseBigDecimal()
        val c = order0.toPreciseBigDecimal()

        return listOf(ImaginaryNumber(-c / m))
    }

    /**
     * Finds the roots of a quadratic equation
     *
     * @param order0 the constant in the equation
     * @param order1 the coefficient of the x term
     * @param order2 the coefficient of the x^2 term
     *
     * @return the two roots of the equation, null if the roots can't
     * be found
     */
    fun rootOfQuadraticEquation(
        order0: String,
        order1: String,
        order2: String
    ): List<ImaginaryNumber?> {
        if (order2.toBigDecimalWithSamePrecision() == BigDecimal.ZERO) {
            return rootOfLinearEquation(order0, order1)
        }

        val a = order2.toPreciseBigDecimal()
        val b = order1.toPreciseBigDecimal()
        val c = order0.toPreciseBigDecimal()
        var d = b.pow(2) - (4.toPreciseBigDecimal() * a * c)
        val denominator = 2.toPreciseBigDecimal() * a
        if (d >= BigDecimal.ZERO) {
            // Real roots
            d = d.roughSqrt()
            val root1 = (-b + d) / denominator
            val root2 = (-b - d) / denominator
            return listOf(ImaginaryNumber(root1), ImaginaryNumber(root2))
        } else {
            // Imaginary roots
            d = d.abs().roughSqrt()
            val real = -b / denominator
            val imaginary = d / denominator
            return listOf(
                ImaginaryNumber(real, imaginary),
                ImaginaryNumber(real, -imaginary)
            )
        }
    }

    /**
     * Finds the roots of a cubic equation
     *
     * @param order0 the constant in the equation
     * @param order1 the coefficient of the x term
     * @param order2 the coefficient of the x^2 term
     * @param order3 the coefficient of the x^3 term
     *
     * @return the three roots of the equation, null if the roots can't
     * be found
     */
    fun rootOfCubicEquation(
        order0: String,
        order1: String,
        order2: String,
        order3: String,
    ): List<ImaginaryNumber?> {
        if (order3.toBigDecimalWithSamePrecision() == BigDecimal.ZERO) {
            return rootOfQuadraticEquation(order0, order1, order2)
        }

        // normalize such that a = 1
        val a = order3.toPreciseBigDecimal()
        val b = order2.toPreciseBigDecimal() / a
        val c = order1.toPreciseBigDecimal() / a
        val d = order0.toPreciseBigDecimal() / a

        // useful values for calculations
        val e = (18.toPreciseBigDecimal() * b * c * d) -
                (4.toPreciseBigDecimal() * b.pow(3) * d) +
                (b.pow(2) * c.pow(2)) -
                (4.toPreciseBigDecimal() * c.pow(3)) -
                (27.toPreciseBigDecimal() * d.pow(2))
        val e0 = b.pow(2) - (3.toPreciseBigDecimal() * c)
        val e1 =
            (2.toPreciseBigDecimal() * b.pow(3)) - (9.toPreciseBigDecimal() * b * c) + (27.toPreciseBigDecimal() * d)

        if (e.signum() == 0) {
            // all three roots are real, with at least one being repeated
            if (e0.signum() == 0) {
                // all three roots are equal
                return listOf(
                    ImaginaryNumber(b / -3.toPreciseBigDecimal()),
                    ImaginaryNumber(b / -3.toPreciseBigDecimal()),
                    ImaginaryNumber(b / -3.toPreciseBigDecimal()),
                )
            } else {
                // two out of the three roots are equal
                return listOf(
                    ImaginaryNumber(
                        ((4.toPreciseBigDecimal() * b * c) - (9.toPreciseBigDecimal() * d) - b.pow(
                            3
                        )) / e0
                    ),
                    ImaginaryNumber(((9.toPreciseBigDecimal() * d) - (b * c)) / (2.toPreciseBigDecimal() * e0)),
                    ImaginaryNumber(((9.toPreciseBigDecimal() * d) - (b * c)) / (2.toPreciseBigDecimal() * e0)),
                )
            }
        } else {
            // if e > 0 only one root is real, the other two are imaginary
            // if e < 0 all three roots are real and distinct
            val pseudoE = (e1.pow(2) - (4.toPreciseBigDecimal() * e0.pow(3)))
            val sqrtE = when {
                pseudoE.stripTrailingZeros() >= BigDecimal.ZERO -> ImaginaryNumber(
                    pseudoE.abs().roughSqrt(),
                )

                else -> ImaginaryNumber(
                    0.toPreciseBigDecimal(),
                    pseudoE.abs().roughSqrt()
                )
            }

            val f = when {
                ((sqrtE + e1).isNonZero()) -> ((sqrtE + e1) / 2.toPreciseBigDecimal()).cubeRoots()
                else -> ((-sqrtE + e1) / 2.toPreciseBigDecimal()).cubeRoots()
            }
            return f.map { element ->
                (element + (element.inverse() * e0) + b) / (-3.toPreciseBigDecimal() * a)
            }
        }
    }
}