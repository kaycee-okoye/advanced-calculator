package com.oaktech.advancedcalculator.controllers

import com.oaktech.advancedcalculator.Constants

/**
 * A class that handles input formatting/validation in secondary input fields
 */
object SimpleInputEditor {
    /**
     * Updates an input when a button on the in-app keyboard is clicked
     *
     * @param currentValue the current text being displayed on the main input field
     * @param input the text displayed on the button that was clicked
     *
     * @return the value to use to update the input field, return null if
     * the inputting [input] results in a formatting error
     *
     */
    fun inputButtonPressed(currentValue: String, input: String): String? {
        if (input == Constants.clearSymbol) {
            return ""
        } else if (input == Constants.deleteSymbol) {
            if (currentValue.length > 1) {
                return currentValue.substring(0, currentValue.lastIndex)
            }
            return ""
        } else if (input == Constants.signSymbol) {
            if (currentValue.isNotBlank()) {
                if (Constants.negativeSign in currentValue) {
                    return currentValue.substring(1)
                } else {
                    return Constants.negativeSign + currentValue
                }
            } else {
                return null
            }
        } else if (input == Constants.decimalSymbol) {
            if (Constants.decimalSymbol in currentValue) {
                return null
            }
            else {
                if (currentValue.isBlank()) {
                    return "0"+Constants.decimalSymbol
                }
                else {
                    return currentValue+Constants.decimalSymbol
                }
            }
        } else {
            return currentValue + input
        }
    }
}