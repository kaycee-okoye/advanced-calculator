package com.oaktech.advancedcalculator.extensions

import com.oaktech.advancedcalculator.Constants
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.sqrt

/**
 * Converts number to BigDecimal with the appropriate
 * number of decimal places for calculations
 *
 * @return corresponding BigDecimal version of the string
 */
fun Int.toPreciseBigDecimal(): BigDecimal {
    return this.toBigDecimal().setScale(Constants.calculationPrecision, RoundingMode.HALF_UP)
}

/**
 * Converts number to BigDecimal with the appropriate
 * number of decimal places for calculations
 *
 * @return corresponding BigDecimal version of the string
 */
fun Double.toPreciseBigDecimal(): BigDecimal {
    return this.toBigDecimal().setScale(Constants.calculationPrecision, RoundingMode.HALF_UP)
}


/**
 * Enforces the precision of final answers, and deletes trailing zeros *
 *
 * @return appropriately scaled number for display
 */
fun BigDecimal.toFinalAnswerPrecision(): BigDecimal {
    return this.setScale(Constants.outputPrecision, RoundingMode.HALF_UP)
}

/**
 * Enforces the precision of final answers, and deletes trailing zeros *
 *
 * @return appropriately formatted final answer
 */
fun BigDecimal.toFinalAnswerPrecisionString(): String {
    return this.setScale(Constants.outputPrecision, RoundingMode.HALF_UP)
        .stripTrailingZeros()
        .toPlainString()
}

/**
 * Calculates the approximate square root of a BigDecimal
 *
 * @return square root of the number
 */
fun BigDecimal.roughSqrt(): BigDecimal {
    return sqrt(this.toDouble()).toBigDecimal()
        .setScale(Constants.calculationPrecision, RoundingMode.HALF_UP)
}

/**
 * Checks whether a number is an integer / whole number i.e. doesn't have decimals
 *
 * @return whether number is a whole number
 */
fun BigDecimal.isWholeNumber(): Boolean {
    return !this.stripTrailingZeros().toPlainString().contains(Constants.decimalSymbol)
}