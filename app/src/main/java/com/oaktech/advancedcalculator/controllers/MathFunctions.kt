package com.oaktech.advancedcalculator.controllers

import com.oaktech.advancedcalculator.Constants
import com.oaktech.advancedcalculator.extensions.enforceFinalAnswerPrecision
import com.oaktech.advancedcalculator.extensions.isWholeNumber
import com.oaktech.advancedcalculator.extensions.replaceSublist
import com.oaktech.advancedcalculator.extensions.roughSqrt
import com.oaktech.advancedcalculator.extensions.toPreciseBigDecimal
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Collections
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.sin
import kotlin.math.tan

/**
 * Contains implementations of mathematical functions used throughout the application
 */
object MathFunctions {
    /**
     * Calculates the [order]th order power of a number [number]
     *
     * This is calculated using logarithms to circumvent the issue
     * of BigDecimals only being able to calculate powers where [order] is +ve integer.
     *
     * @param number the number whose power should be calculated
     * @param order the order of the power to calculate e.g. c = 2 is square, c = 3
     * is cube
     *
     * @return the [order]th order power of the number
     */
    fun power(number: BigDecimal, order: BigDecimal): BigDecimal {
        if (order.signum() < 0) {
            return 1.toPreciseBigDecimal() / power(number, order.abs())
        }
        if (order.isWholeNumber()) {
            return number.pow(order.toInt())
        } else {
            return Math.exp(
                (Math.log(number.toDouble()).toPreciseBigDecimal() * order).toDouble()
            ).toPreciseBigDecimal()
        }
    }

    /**
     * Calculates the [order]th order root of a number [number]
     *
     * Specifically, this function calculates number ^ (1/order).
     * This is calculated using logarithms to circumvent the issue
     * of BigDecimals not being able to perform root calculations.
     *
     * @param number the number whose root should be calculated
     * @param order the order of the root to calculate e.g. c = 2 is square root, c = 3
     * is cube root
     *
     * @return the [order]th order root of the number
     */
    fun root(number: BigDecimal, order: BigDecimal): BigDecimal {
        val n = 1.toPreciseBigDecimal() / order
        return Math.exp(
            (Math.log(number.toDouble()).toPreciseBigDecimal() * n).toDouble()
        ).toPreciseBigDecimal()
    }

    /**
     * Calculates the factorial of a number
     *
     * If [number] has a decimal component, the factorial of the highest integer
     * less than [number] is calculated
     *
     * @param number the number whose factorial should be calculated
     *
     * @return the factorial of the number
     */
    fun factorial(number: BigDecimal): BigDecimal {
        if (number < BigDecimal.ZERO) {
            throw IllegalArgumentException("Can't find factorial of a negative number")
        } else if (number == BigInteger.ZERO) {
            return 1.toPreciseBigDecimal()
        } else {
            var result = 1.toPreciseBigDecimal()
            for (count in 1..number.toInt()) {
                result *= count.toBigDecimal()
            }
            return result
        }
    }

    /**
     * Calculates the permutation [left]P[right] of two numbers
     *
     * A permutation is the choice of [right] number of things from a set of [left] number of
     * things without replacement and where the order matters.
     *
     * @param left the number on the left of the operator
     * @param right the number on the right of the operator
     *
     * @return number of permutations
     */
    fun permutation(left: BigDecimal, right: BigDecimal): BigDecimal {
        return factorial(left) / factorial(left - right)
    }

    /**
     * Calculates the combination [left]C[right] of two numbers
     *
     * A combination is the choice of [right] number of things from
     * a set of [left] number of n things without
     * replacement and where order does not matter.
     *
     * @param left the number on the left of the operator
     * @param right the number on the right of the operator
     *
     * @return number of combinations
     */
    fun combination(left: BigDecimal, right: BigDecimal): BigDecimal {
        return factorial(left) / (factorial(left - right) * factorial(right))
    }

    /**
     * Converts a number from degree to radians
     *
     * @param degrees number in degrees
     *
     * @return number in radians
     */
    fun degreesToRadians(degrees: BigDecimal): BigDecimal {
        return degrees * (Math.PI / 180).toBigDecimal()
    }

    /**
     * Converts a number from radians to degree
     *
     * @param degrees number in degrees
     *
     * @return radians in radians
     */
    fun radiansToDegrees(radians: BigDecimal): BigDecimal {
        return radians * (180 / Math.PI).toBigDecimal()
    }

    /**
     * Calculates the result of a left/right bracket operation
     *
     * Left bracket operations will take precedence if both a left and right
     * bracket operation are provided
     *
     * @param value the number to apply the operation to
     * @param leftBracket the left bracket symbol. Used to detect left-bracket operations
     * @param rightBracket the right bracket symbol. Used to detect right-bracket operations
     * @param isInRadians whether the assume numbers are in radians for trig operations,
     * uses [degreesToRadians] to convert to radians otherwise
     *
     * @return the result of the operation
     */
    fun applyFunction(
        value: String,
        leftBracket: String,
        rightBracket: String,
        isInRadians: Boolean
    ): String {
        val valueAsBigDecimal = value.toPreciseBigDecimal()
        val afterLeftBracket = when (leftBracket) {
            Constants.sqrtSymbol -> valueAsBigDecimal.roughSqrt()
            Constants.invSymbol -> 1.toPreciseBigDecimal() / valueAsBigDecimal
            Constants.sinSymbol -> sin((if (!isInRadians) degreesToRadians(valueAsBigDecimal) else valueAsBigDecimal).toDouble()).toPreciseBigDecimal()
            Constants.cosSymbol -> cos((if (!isInRadians) degreesToRadians(valueAsBigDecimal) else valueAsBigDecimal).toDouble()).toPreciseBigDecimal()
            Constants.tanSymbol -> {
                if (valueAsBigDecimal == 90.toPreciseBigDecimal()) {
                    throw IllegalArgumentException("90 degrees is outside the domain of tan")
                }
                tan((if (!isInRadians) degreesToRadians(valueAsBigDecimal) else valueAsBigDecimal).toDouble()).toPreciseBigDecimal()
            }

            Constants.invSinSymbol -> radiansToDegrees(asin(valueAsBigDecimal.toDouble()).toPreciseBigDecimal())
            Constants.invCosSymbol -> radiansToDegrees(acos(valueAsBigDecimal.toDouble()).toPreciseBigDecimal())
            Constants.invTanSymbol -> radiansToDegrees(atan(valueAsBigDecimal.toDouble()).toPreciseBigDecimal())
            Constants.logSymbol -> log10(valueAsBigDecimal.toDouble()).toPreciseBigDecimal()
            Constants.lnSymbol -> ln(valueAsBigDecimal.toDouble()).toPreciseBigDecimal()
            Constants.expSymbol -> exp(valueAsBigDecimal.toDouble()).toPreciseBigDecimal()
            else -> valueAsBigDecimal
        }
        val afterRightBracket = when (rightBracket) {
            Constants.squareSymbol -> afterLeftBracket.pow(2)
            Constants.facSymbol -> factorial(afterLeftBracket)
            Constants.percentSymbol -> afterLeftBracket / (100).toBigDecimal()
            else -> afterLeftBracket
        }
        return afterRightBracket.toString()
    }

    /**
     * Calculates the value of a function
     *
     * @param originalInput the function to be solved, should contain no variables
     *
     * @return the solution to the user input
     */
    fun solveFunction(originalInput: MutableList<String>): MutableList<String> {
        var inputs = originalInput;
        val bracketStartIndices = mutableListOf<Int>();
        val absStartIndices = mutableListOf<Int>();
        var i = 0
        while (i < inputs.size) {
            // systematically solve and remove parts enclosed in brackets in the
            // appropriate order
            if (inputs[i].endsWith(Constants.bracketOpen)) {
                bracketStartIndices.add(i)
            } else if (inputs[i] === Constants.leftAbsBracket) {
                absStartIndices.add(i)
            } else if (inputs[i].startsWith(Constants.bracketClose)) {
                inputs = replaceSublist(
                    originalList = inputs,
                    startIndex = bracketStartIndices.last(),
                    stopIndex = i,
                    newValue = performArithmeticCalculation(
                        inputs.subList(
                            bracketStartIndices.last(),
                            i + 1
                        )
                    )
                )
                i = bracketStartIndices.last()
                bracketStartIndices.removeLast()
            } else if (inputs[i] == Constants.rightAbsBracket) {
                inputs = replaceSublist(
                    originalList = inputs,
                    startIndex = absStartIndices.last(),
                    stopIndex = i,
                    newValue = performArithmeticCalculation(
                        inputs.subList(
                            absStartIndices.last(),
                            i + 1
                        )
                    )
                )
                i = absStartIndices.last()
                absStartIndices.removeLast()
            }
            i += 1
        }
        // final solution of the problem
        inputs = replaceSublist(
            originalList = inputs,
            startIndex = 0,
            stopIndex = inputs.size - 1,
            newValue = performArithmeticCalculation(inputs).enforceFinalAnswerPrecision()
        )
        return inputs
    }

    /**
     * Perform simple arithmetic calculations on an input
     *
     * @param input arithmetic problem to be solved. Should be properly
     * formatted and have nothing except numbers and arithmetic operators.
     *
     * @return the solution to the arithmetic problem
     */
    fun performArithmeticCalculation(
        originalInput: List<String>,
    ): String {
        var input = originalInput.toMutableList()
        val containsPi = input.contains(Constants.piSymbol)
        Collections.replaceAll(input, Constants.piSymbol, Math.PI.toString())
        Collections.replaceAll(input, Constants.eulerSymbol, Math.E.toString())

        // solve middle operators starting with BODMAS order then
        // other middle operators
        val bodmas = listOf<List<String>>(
            listOf(Constants.powerSymbol),
            listOf(Constants.multiplySymbol, Constants.divideSymbol),
            listOf(Constants.minusSymbol, Constants.plusSymbol),
            Constants.middleOperations
        )
        for (stage: List<String> in bodmas) {
            var i = 0
            while (i < input.size) {
                if (input[i] in stage) {
                    val operandIndex = i
                    val left = input[operandIndex - 1].toPreciseBigDecimal()
                    val right = input[operandIndex + 1].toPreciseBigDecimal()

                    val result = when (input[operandIndex]) {
                        Constants.plusSymbol -> left + right
                        Constants.minusSymbol -> left - right
                        Constants.multiplySymbol -> left * right
                        Constants.divideSymbol -> left / right
                        Constants.permSymbol -> MathFunctions.permutation(
                            left,
                            right
                        )

                        Constants.combSymbol -> MathFunctions.combination(
                            left,
                            right
                        )

                        Constants.powerSymbol -> power(left, right)

                        else -> BigDecimal.ZERO
                    }

                    input = replaceSublist(
                        originalList = input,
                        startIndex = operandIndex - 1,
                        stopIndex = operandIndex + 1,
                        newValue = result.toString()
                    )
                    i = operandIndex - 1
                }
                i += 1
            }
        }

        // if the problem was enclosed in a bracket function,
        // apply the bracket function to the result to get the
        // final result
        if (input.size > 1) {
            val leftBracket = input.first()
            val value = input[1]
            val rightBracket = input.last()
            input = replaceSublist(
                input,
                0,
                input.size - 1,
                applyFunction(
                    value = value,
                    leftBracket = if (leftBracket.length > 1) leftBracket.substring(
                        0,
                        leftBracket.length - 1
                    ) else leftBracket,
                    rightBracket = if (rightBracket.length > 1) rightBracket.substring(
                        1
                    ) else rightBracket,
                    isInRadians = containsPi
                )
            )
        }
        return input.joinToString("")
    }
}