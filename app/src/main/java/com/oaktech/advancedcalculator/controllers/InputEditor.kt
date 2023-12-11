package com.oaktech.advancedcalculator.controllers

import com.oaktech.advancedcalculator.Constants
import com.oaktech.advancedcalculator.extensions.isNumber
import java.lang.Math.max
import java.lang.Math.min
import java.util.Collections

/**
 * A class that handles input formatting/validation in the main input field
 */
class InputEditor {
    // variables used for calculations
    private var inputs = mutableListOf<String>() // input being displayed
    private var openBracketCount = 0 // the number of unclosed brackets
    private var openAbsCount = 0 // the number of unclosed abs value brackets

    /**
     * Verifies whether a new input from the in-app keyboard can be added to the MainInputField
     *
     * @return the new text to be displayed on the MainInputField, returns null if the [input] can
     * not legally be added
     */
    fun addInput(input: String): String? {

        val lastInput = inputs.lastOrNull()

        // adding a number/constant symbol
        if ((input in Constants.numbers) || (input in Constants.constantSymbols) &&
            (lastInput == null ||
                    lastInput.isNumber() || lastInput in Constants.constantSymbols ||
                    lastInput in Constants.arithmeticOperators + Constants.middleOperations ||
                    lastInput.endsWith(Constants.bracketOpen) || lastInput == Constants.leftAbsBracket ||
                    lastInput.startsWith(Constants.bracketClose) || lastInput == Constants.rightAbsBracket
                    )
        ) {
            if (lastInput?.isNumber() ?: false) {
                if (input in Constants.constantSymbols) {
                    // auto-formatting
                    inputs.add(Constants.multiplySymbol)
                    inputs.add(input)
                } else {
                    inputs[inputs.size - 1] = lastInput + input
                }
            } else if (lastInput?.startsWith(Constants.bracketClose) ?: false || lastInput == Constants.rightAbsBracket) {
                inputs.add(Constants.multiplySymbol)
                inputs.add(input)
            } else if (lastInput in Constants.constantSymbols) {
                // auto-formatting
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
                    lastInput.isNumber() || lastInput in Constants.constantSymbols ||
                    lastInput in Constants.arithmeticOperators + Constants.middleOperations ||
                    lastInput.endsWith(Constants.bracketOpen) || lastInput == Constants.leftAbsBracket ||
                    lastInput.startsWith(Constants.bracketClose) || lastInput == Constants.rightAbsBracket
                    )
        ) {
            if (lastInput?.isNumber() ?: false) {
                if ((lastInput + Constants.decimalSymbol).isNumber()) {
                    inputs[inputs.size - 1] = lastInput + input
                } else {
                    return null
                }
            } else if (lastInput?.startsWith(Constants.bracketClose) ?: false ||
                lastInput == Constants.rightAbsBracket ||
                lastInput in Constants.constantSymbols
            ) {
                inputs.add(Constants.multiplySymbol)
                inputs.add("0.")
            } else {
                inputs.add("0.")
            }
            return displayFunction()
        }

        // adding an operator/middle function
        else if ((input in Constants.arithmeticOperators + Constants.middleOperations) &&
            (lastInput?.isNumber() ?: false || lastInput in Constants.constantSymbols ||
                    lastInput?.startsWith(Constants.bracketClose) ?: false
                    )
        ) {
            inputs.add(input)
            return displayFunction()
        }

        // adding a bracket
        else if (input == Constants.bracketSymbol) {
            if (
                lastInput == null ||
                lastInput.endsWith(Constants.bracketOpen) ||
                lastInput in Constants.arithmeticOperators + Constants.middleOperations
            ) {
                inputs.add(Constants.bracketOpen)
                openBracketCount += 1
                return displayFunction()
            } else if ((lastInput.isNumber() || lastInput in Constants.constantSymbols) && openBracketCount == 0) {
                // auto-formatting
                inputs.add(Constants.multiplySymbol)
                inputs.add(Constants.bracketOpen)
                openBracketCount += 1
                return displayFunction()
            } else if (lastInput.startsWith(Constants.bracketClose)) {
                inputs.add(Constants.multiplySymbol)
                inputs.add(Constants.bracketOpen)
                openBracketCount += 1
                return displayFunction()
            } else if (openBracketCount > 0) {
                // make sure this doesn't happen -> ( | x ) |
                var subOpenAbsBracketCount = 0
                for (i in inputs.size - 1 downTo 0) {
                    if (inputs[i].endsWith(Constants.bracketOpen)) {
                        break
                    } else if (inputs[i] == Constants.leftAbsBracket) {
                        subOpenAbsBracketCount += 1
                    } else if (inputs[i] == Constants.rightAbsBracket) {
                        subOpenAbsBracketCount -= 1
                    }
                }
                while (subOpenAbsBracketCount > 0) {
                    // auto-formatting
                    inputs.add(Constants.rightAbsBracket)
                }
                inputs.add(Constants.bracketClose)
                openBracketCount -= 1
                return displayFunction()
            }
            return null
        }

        // adding abs bracket
        else if (input == Constants.absSymbol) {
            if (
                lastInput == null ||
                lastInput.endsWith(Constants.bracketOpen) ||
                lastInput in Constants.arithmeticOperators + Constants.middleOperations
            ) {
                inputs.add(Constants.leftAbsBracket)
                openAbsCount += 1
                return displayFunction()
            } else if ((lastInput.isNumber() || lastInput in Constants.constantSymbols) && openBracketCount == 0) {
                // auto-formatting
                inputs.add(Constants.multiplySymbol)
                inputs.add(Constants.leftAbsBracket)
                openAbsCount += 1
                return displayFunction()
            } else if (lastInput.startsWith(Constants.bracketClose)) {
                inputs.add(Constants.multiplySymbol)
                inputs.add(Constants.leftAbsBracket)
                openBracketCount += 1
                return displayFunction()
            } else if (openAbsCount > 0) {
                // make sure this doesn't happen -> ( | x ) |
                var subOpenBracketCount = 0
                for (i in inputs.size - 1 downTo 0) {
                    if (inputs[i] == Constants.leftAbsBracket) {
                        break
                    } else if (inputs[i].endsWith(Constants.bracketOpen)) {
                        subOpenBracketCount += 1
                    } else if (inputs[i].startsWith(Constants.bracketClose)) {
                        subOpenBracketCount -= 1
                    }
                }
                while (subOpenBracketCount > 0) {
                    // auto-formatting
                    inputs.add(Constants.bracketClose)
                }
                inputs.add(Constants.rightAbsBracket)
                openAbsCount -= 1
                return displayFunction()
            }
            return null
        }

        // adding a left bracket function
        else if (input in Constants.leftBracketOperations) {
            if (
                lastInput == null ||
                lastInput.endsWith(Constants.bracketOpen) ||
                lastInput in Constants.arithmeticOperators + Constants.middleOperations
            ) {
                inputs.add(input + Constants.bracketOpen)
                openBracketCount += 1
                return displayFunction()
            } else if (lastInput.isNumber() && openBracketCount == 0) {
                // auto-formatting
                inputs.add(Constants.multiplySymbol)
                inputs.add(input + Constants.bracketOpen)
                openBracketCount += 1
                return displayFunction()
            }
            return null
        }

        // adding a right bracket function
        else if (input in Constants.rightBracketOperations) {
            if (!(
                        lastInput == null ||
                                lastInput.endsWith(Constants.bracketOpen) ||
                                lastInput in Constants.arithmeticOperators + Constants.middleOperations
                        )
            ) {
                if (!(lastInput.isNumber() && openBracketCount == 0)) {
                    if (openBracketCount > 0) {
                        inputs.add(Constants.bracketClose + input)
                        openBracketCount -= 1
                        return displayFunction()
                    }
                }
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
                // remove negative sign if exists
                inputs[inputs.size - 1] = lastInput.substring(1)
            } else {
                // add negative sign if none present
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
            if (lastInput.isNumber() || lastInput in Constants.constantSymbols) {
                if (lastInput.isNumber() && lastInput.replace(
                        Constants.negativeSign,
                        ""
                    ).length > 1
                ) {
                    inputs[inputs.size - 1] = lastInput.substring(0, lastInput.length - 1)
                } else {
                    inputs.removeLast()
                }
            } else if (lastInput in Constants.arithmeticOperators + Constants.middleOperations) {
                inputs.removeLast()
            } else if (lastInput == Constants.rightAbsBracket) {
                openAbsCount += 1
                inputs.removeLast()
            } else if (lastInput == Constants.leftAbsBracket) {
                openAbsCount -= 1
                inputs.removeLast()
            } else if (lastInput.startsWith(Constants.bracketClose)) {
                openBracketCount += 1
                inputs.removeLast()
            } else if (lastInput.endsWith(Constants.bracketOpen)) {
                openBracketCount -= 1
                inputs.removeLast()
            } else if (lastInput == Constants.errorSymbol) {
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
        openAbsCount = 0
        return ""
    }

    /**
     * Prepare the text being displayed in the MainInputField for solving if '=' is pressed
     *
     * @param the value of the independent variable (if any), optional
     *
     * @return new string to displayed, in case any adjustments where made to the user input
     */
    fun done(independentVariableValue: String = ""): String? {
        val lastInput = inputs.lastOrNull()
        if (lastInput != Constants.errorSymbol && (lastInput == null ||
                    (lastInput != Constants.leftAbsBracket && !lastInput.endsWith(Constants.bracketOpen) && min(
                        openBracketCount,
                        openAbsCount
                    ) == 0)
                    )
        ) {
            // remove trailing middle operators
            while (
                inputs.isNotEmpty() &&
                (inputs[inputs.size - 1] in Constants.arithmeticOperators ||
                        inputs[inputs.size - 1] in Constants.middleOperations)
            ) {
                inputs.removeLast()
            }

            // close open brackets and abs brackets in the appropriate order
            val bracketStartIndices = mutableListOf<Int>();
            val absStartIndices = mutableListOf<Int>();
            for (i in 0..inputs.size - 1) {
                if (inputs[i].endsWith(Constants.bracketOpen)) {
                    bracketStartIndices.add(i)
                } else if (inputs[i].startsWith(Constants.bracketClose)) {
                    bracketStartIndices.removeLast()
                } else if (inputs[i] == Constants.leftAbsBracket) {
                    absStartIndices.add(i)
                } else if (inputs[i] == Constants.rightAbsBracket) {
                    absStartIndices.removeLast()
                }
            }
            var bracketI = bracketStartIndices.size - 1
            var absI = absStartIndices.size - 1
            while (max(bracketI, absI) >= 0) {
                if (bracketI == -1 || (absI >= 0 && bracketStartIndices[bracketI] < absStartIndices[absI])) {
                    inputs.add(Constants.rightAbsBracket)
                    openAbsCount -= 1
                    absI -= 1
                } else if (absI == -1 || (bracketI >= 0 && bracketStartIndices[bracketI] > absStartIndices[absI])) {
                    inputs.add(Constants.bracketClose)
                    openBracketCount -= 1
                    bracketI -= 1
                }
            }

            if (inputs.contains(Constants.independentVariable)) {
                if (independentVariableValue.isNumber()) {
                    Collections.replaceAll(
                        inputs,
                        Constants.independentVariable,
                        independentVariableValue
                    )
                }
                else {
                    return displayFunction()
                }
            }
            try {
                inputs = MathFunctions.solveFunction(inputs)
            } catch (e: Exception) {
                inputs = mutableListOf<String>(Constants.errorSymbol)
            }
            return displayFunction()
        }
        return null
    }

    /**
     * Creates the text to be displayed in the MainInputField
     *
     * @return String the text to be displayed i.e. [inputs].joinToString
     */
    fun displayFunction(): String {
        return inputs.joinToString("")
    }

    /**
     * Function to manually set the [inputs] list
     *
     * This function is particularly useful in testing.
     * It will also count the number of [openAbsCount]
     * and [openBracketCount]. However, it doesn't perform
     * a formatting check on [inputs].
     *
     * @param inputs the inputs to be displayed
     */
    fun setInputs(inputs: MutableList<String>) {
        this.inputs = inputs
        openBracketCount = 0
        openAbsCount = 0
        for (input in inputs) {
            if (input.endsWith(Constants.bracketOpen)) {
                openBracketCount += 1
            } else if (input.startsWith(Constants.bracketClose)) {
                openBracketCount -= 1
            } else if (input == Constants.leftAbsBracket) {
                openAbsCount += 1
            } else if (input == Constants.rightAbsBracket) {
                openAbsCount -= 1
            }
        }
    }

    fun getInputs(): MutableList<String> {
        return inputs
    }

}