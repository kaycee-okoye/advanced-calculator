package com.oaktech.advancedcalculator.extensions

import com.oaktech.advancedcalculator.Constants
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Verifies whether a String represents a number
 *
 * @return whether the string represents a number
 */
fun String.isNumber(): Boolean {
    return this.toDoubleOrNull() != null;
}

/**
 * Converts number string to BigDecimal with the same precision as the string form
 *
 * @return corresponding BigDecimal version of the string
 */
fun String.toBigDecimalWithSamePrecision(): BigDecimal {
    val currentScale = when {
        this.contains(Constants.decimalSymbol) -> this.lastIndex - this.indexOf(Constants.decimalSymbol)
        else -> 0
    }
    return this.toBigDecimal().setScale(currentScale, RoundingMode.HALF_UP)
}

/**
 * Converts number string to BigDecimal with the appropriate
 * number of decimal places for calculations
 *
 * @return corresponding BigDecimal version of the string
 */
fun String.toPreciseBigDecimal(): BigDecimal {
    return this.toBigDecimal().setScale(Constants.calculationPrecision, RoundingMode.HALF_UP)
}

/**
 * Enforces the precision of final answers, and deletes trailing zeros *
 *
 * @return appropriately formatted final answer
 */
fun String.enforceFinalAnswerPrecision(): String {
    return this.toPreciseBigDecimal().setScale(Constants.outputPrecision, RoundingMode.HALF_UP)
        .stripTrailingZeros()
        .toPlainString()
}