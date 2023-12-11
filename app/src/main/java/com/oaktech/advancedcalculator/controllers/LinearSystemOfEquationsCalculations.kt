package com.oaktech.advancedcalculator.controllers

import com.oaktech.advancedcalculator.Constants
import com.oaktech.advancedcalculator.extensions.enforceFinalAnswerPrecision
import com.oaktech.advancedcalculator.extensions.toPreciseBigDecimal
import java.math.BigDecimal

/**
 * Implements Gauss elimination row reduction to solve a linear system of equations
 *
 * @param coeffs the coefficients of the variables in each equation. Each row (i.e. sublist)
 * in [coeffs] represents the coefficients of an equation in the system.
 */
class LinearSystemOfEquationsCalculations(private var coeffs: MutableList<MutableList<String>>) {
    private var rowReducedMatrix =
        coeffs.map { row -> row.map { cell -> cell.toPreciseBigDecimal() }.toMutableList() }
            .toMutableList()

    private val lastRow = coeffs.size - 1
    private val lastCol = coeffs.first().size - 1


    init {
        for (colIndex in 0..lastCol - 2) {

            // if necessary, swap two rows to make sure diagonal cell
            // (colIndex, rowIndex) is non-zero
            var isAllZeroColumn = true
            for (rowIndex in colIndex..lastRow) {
                if (rowReducedMatrix[rowIndex][colIndex]
                        .stripTrailingZeros() != BigDecimal.ZERO
                ) {
                    isAllZeroColumn = false
                    swapRows(sourceRowIndex = colIndex, destinationRowIndex = rowIndex)
                    break
                }
            }

            if (!isAllZeroColumn) {
                // perform row-reduction for each subsequent row targeted at the current column
                for (rowIndex in colIndex + 1..lastRow) {
                    reduceRow(anchorRowIndex = colIndex, targetRowIndex = rowIndex)
                }
            }
        }
    }

    /**
     * Performs row reduction on row [targetRowIndex]
     *
     * @param anchorRowIndex the index of the current anchor row. This
     * row will be used to reduce [targetRowIndex]
     * @param targetRowIndex the index of the row to reduce
     */
    private fun reduceRow(anchorRowIndex: Int, targetRowIndex: Int) {
        if (rowReducedMatrix[targetRowIndex][anchorRowIndex].stripTrailingZeros() != BigDecimal.ZERO) {
            val reductionRatio =
                rowReducedMatrix[targetRowIndex][anchorRowIndex] /
                        rowReducedMatrix[anchorRowIndex][anchorRowIndex]
            for (colIndex in anchorRowIndex..lastCol) {
                rowReducedMatrix[targetRowIndex][colIndex] =
                    rowReducedMatrix[targetRowIndex][colIndex] -
                            (rowReducedMatrix[anchorRowIndex][colIndex] * reductionRatio)
            }
        }
    }

    /**
     * Swaps two rows in [rowReducedMatrix]
     *
     * @param sourceRowIndex the index of the row to swap
     * @param destinationRowIndex the index of the row to swap with
     */
    private fun swapRows(sourceRowIndex: Int, destinationRowIndex: Int) {
        if (sourceRowIndex != destinationRowIndex) {
            val temp = rowReducedMatrix[sourceRowIndex]
            rowReducedMatrix[sourceRowIndex] = rowReducedMatrix[destinationRowIndex]
            rowReducedMatrix[destinationRowIndex] = temp
        }
    }

    /**
     * Calculates the solutions to the linear system of equations using [rowReducedMatrix]
     *
     * @return solutions for each variable, an empty list if no solution exists
     */
    fun solution(): List<String>? {
        val values = mutableListOf<String>()
        for (rowIndex in lastRow downTo 0) {
            val targetCoeff = rowReducedMatrix[rowIndex][rowIndex] // coefficient of variable
            // being solved for
            val constantEquator = rowReducedMatrix[rowIndex][lastCol] // constant on
            // right side of '='
            if (targetCoeff.stripTrailingZeros() == BigDecimal.ZERO &&
                constantEquator.stripTrailingZeros() != BigDecimal.ZERO
            ) {
                // no solution exists
                return null
            }

            // sum of other variables in the current equation whose
            // values are known
            var sumOfOtherContributions = 0.toPreciseBigDecimal()
            var isAllZeroRow = true
            for (colIndex in rowIndex + 1..lastCol - 1) {
                val coeff = rowReducedMatrix[rowIndex][colIndex]
                if (coeff.stripTrailingZeros() != BigDecimal.ZERO) {
                    isAllZeroRow = false
                } else {
                    continue
                }

                val scale = values[lastRow - colIndex]
                sumOfOtherContributions += coeff * scale.toPreciseBigDecimal()
            }

            if (isAllZeroRow && targetCoeff.stripTrailingZeros() == BigDecimal.ZERO) {
                // infinite solutions exist
                return listOf(Constants.infinitySymbol)
            } else {
                // solution of the variable
                values.add(((constantEquator - sumOfOtherContributions) / targetCoeff).toPlainString())
            }
        }
        return values.asReversed().map { it.enforceFinalAnswerPrecision() }
    }
}