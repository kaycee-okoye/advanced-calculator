package com.oaktech.advancedcalculator.controllers

import com.oaktech.advancedcalculator.Constants
import com.oaktech.advancedcalculator.extensions.isNumber
import com.oaktech.advancedcalculator.extensions.replaceSublist
import java.math.BigDecimal

/**
 * A class that handles input formatting/validation in the main input field
 */
class BasicInputEditor {
    // variables used for calculations
    private var inputs = mutableListOf<String>() // input being displayed
    private var openBracketCount = 0 // the number of unclosed brackets

    /**
     * Verifies whether a new input from the in-app keyboard can be added to the MainInputField
     *
     * @return the new text to be displayed on the MainInputField, returns null if the input can
     * not legally be added
     */
    fun addInput(input: String): String? {
        val lastInput = inputs.lastOrNull()

        // adding a number
        if (input in Constants.numbers &&
            (lastInput == null ||
                    lastInput.isNumber() ||
                    lastInput in Constants.arithmeticOperators ||
                    lastInput == Constants.bracketOpen ||
                    lastInput == Constants.bracketClose
                    )
        ) {
            if (lastInput?.isNumber() ?: false) {
                inputs[inputs.size - 1] = lastInput + input
            } else if (lastInput == Constants.bracketClose) {
                inputs.add(Constants.multiplySymbol)
                inputs.add(input)
            } else {
                inputs.add(input)
            }
            return displayFunction()
        }

        // adding a decimal
        else if (input == Constants.decimalSymbol &&
            (lastInput == null ||
                    lastInput.isNumber() ||
                    lastInput in Constants.arithmeticOperators ||
                    lastInput == Constants.bracketOpen ||
                    lastInput == Constants.bracketClose
                    )
        ) {
            if (lastInput?.isNumber() ?: false) {
                if ((lastInput + Constants.decimalSymbol).isNumber()) {
                    inputs[inputs.size - 1] = lastInput + input
                } else {
                    return null
                }
            } else if (lastInput == Constants.bracketClose) {
                inputs.add(Constants.multiplySymbol)
                inputs.add("0.")
            } else {
                inputs.add("0.")
            }
            return displayFunction()
        }

        // adding an operator
        else if (input in Constants.arithmeticOperators &&
            (lastInput?.isNumber() ?: false ||
                    lastInput == Constants.bracketClose
                    )
        ) {
            inputs.add(input)
            return displayFunction()
        }

        // adding a bracket
        else if (input == Constants.bracketSymbol) {
            if (
                lastInput == null ||
                lastInput == Constants.bracketOpen ||
                lastInput in Constants.arithmeticOperators
            ) {
                inputs.add(Constants.bracketOpen)
                openBracketCount += 1
                return displayFunction()
            } else if (lastInput.isNumber() && openBracketCount == 0) {
                inputs.add(Constants.multiplySymbol)
                inputs.add(Constants.bracketOpen)
                openBracketCount += 1
                return displayFunction()
            } else if (openBracketCount > 0) {
                inputs.add(Constants.bracketClose)
                openBracketCount -= 1
                return displayFunction()
            }
            return null
        }

        return null
    }

    /**
     * Toggles the sign of the last number added to MainInputField
     *
     * @return the new text to be displayed on the MainInputField, returns null if the input can
     * not legally be added
     */
    fun signLastNumber(): String? {
        val lastInput = inputs.lastOrNull()
        if (lastInput != null && lastInput.isNumber()) {
            if (Constants.negativeSign in lastInput) {
                inputs[inputs.size - 1] = lastInput.substring(1)
            } else {
                inputs[inputs.size - 1] = Constants.negativeSign + lastInput
            }
            return displayFunction()
        }
        return null
    }

    /**
     * Performs a backspace i.e. undoes last [addInput] call
     *
     * @return the new text to be displayed on the MainInputField
     */
    fun backspace(): String {
        val lastInput = inputs.lastOrNull()
        if (lastInput != null) {
            if (lastInput.isNumber()) {
                if (lastInput.replace(Constants.negativeSign, "").length > 1) {
                    inputs[inputs.size - 1] = lastInput.substring(0, lastInput.length - 1)
                } else {
                    inputs.removeLast()
                }
            } else if (lastInput in Constants.arithmeticOperators) {
                inputs.removeLast()
            } else if (lastInput == Constants.bracketClose) {
                openBracketCount += 1
                inputs.removeLast()
            } else if (lastInput == Constants.bracketOpen) {
                openBracketCount -= 1
                inputs.removeLast()
            }

        }
        return displayFunction()
    }

    /**
     * Clears the text being displayed in the MainInputField
     *
     * @return empty string
     */
    fun clear(): String {
        inputs.clear()
        openBracketCount = 0
        return ""
    }

    /**
     * Prepare the text being displayed in the MainInputField for solving if '=' is pressed
     *
     * @return new string to displayed, in case any adjustments where made to the user input
     */
    fun done(): String? {
        val lastInput = inputs.lastOrNull()
        if (lastInput == null || lastInput != Constants.bracketOpen) {
            while (inputs.isNotEmpty() && inputs[inputs.size - 1] in Constants.arithmeticOperators) {
                inputs.removeLast()
            }
            while (openBracketCount > 0) {
                inputs.add(Constants.bracketClose)
                openBracketCount -= 1
            }
            solve()
            return displayFunction()
        }
        return null
    }

    /**
     * Calculates the solution to the input being displayed in the MainInputField
     *
     * @return the solution to the user input
     */
    fun solve(): String {
        val bracketStartIndices = mutableListOf<Int>();
        var i = 0
        while (i < inputs.size) {
            if (inputs[i] == Constants.bracketOpen) {
                bracketStartIndices.add(i)
            } else if (inputs[i] == Constants.bracketClose) {
                inputs = replaceSublist(
                    inputs,
                    bracketStartIndices.last(),
                    i,
                    performArithmetics(inputs.subList(bracketStartIndices.last(), i + 1))
                )
                i = bracketStartIndices.last()
                bracketStartIndices.removeLast()
            }
            i += 1
        }
        inputs = replaceSublist(
            inputs,
            0,
            inputs.size - 1,
            performArithmetics(inputs)
        )
        return displayFunction()
    }

    /**
     * Perform simple arithmetic calculation on an input
     *
     * @param input arithmetic problem to be solved. Should be properly
     * formatted and have nothing except numbers and arithmetic operators.
     *
     * @return the solution to the arithmetic problem
     */
    fun performArithmetics(
        originalInput: List<String>,
    ): String {
        var input = originalInput.toMutableList()
        for (operand in Constants.arithmeticOperators) {
            var operandIndex = input.indexOf(operand)
            while (operandIndex != -1) {
                val left = input[operandIndex - 1].toBigDecimal()
                val right = input[operandIndex + 1].toBigDecimal()

                val result = when (operand) {
                    Constants.plusSymbol -> left + right
                    Constants.minusSymbol -> left - right
                    Constants.multiplySymbol -> left * right
                    Constants.divideSymbol -> left / right
                    else -> BigDecimal.ZERO
                }
                input = replaceSublist(
                    input,
                    operandIndex - 1,
                    operandIndex + 1,
                    result.toPlainString()
                )
                operandIndex = input.indexOf(operand)
            }
        }
        return input.joinToString("")
    }

    /**
     * Creates the text to be displayed in the MainInputField
     *
     * @return String the text to be displayed i.e. [inputs].joinToString
     */
    fun displayFunction(): String {
        return inputs.joinToString("")
    }
}