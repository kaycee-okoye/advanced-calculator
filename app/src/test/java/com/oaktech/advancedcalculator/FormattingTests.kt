package com.oaktech.advancedcalculator

import com.oaktech.advancedcalculator.controllers.InputEditor
import com.oaktech.advancedcalculator.extensions.isNumber
import org.junit.Assert
import org.junit.Test

/**
 * Contains Unit Tests that ensure that user input formatting
 * is operating as expected.
 */
class FormattingTests {

    @Test
    fun `Test inputting numbers`() {
        val inputs = listOf<String>("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        val errorMessages = getAssertionErrorMessages()
        val inputEditor = InputEditor()
        for (input in inputs) {
            var index = 0
            for (case in getInputEndingCases()) {
                inputEditor.setInputs(case)
                val result = when {
                    case.isEmpty() -> case + input
                    case.last().isNumber() -> case.subList(
                        0,
                        case.lastIndex
                    ) + (case.last() + input)

                    case.last() in Constants.constantSymbols || case.last()
                        .startsWith(Constants.bracketClose) -> case + listOf<String>(
                        Constants.multiplySymbol,
                        input
                    )

                    else -> (case + input)
                }
                Assert.assertEquals(
                    errorMessages[index],
                    result.joinToString(""),
                    inputEditor.addInput(input),
                )
                index += 1
            }
        }
    }

    @Test
    fun `Test inputting decimals`() {
        val inputs = listOf<String>(Constants.decimalSymbol)
        val errorMessages = getAssertionErrorMessages()
        val inputEditor = InputEditor()
        for (input in inputs) {
            var index = 0
            for (case in getInputEndingCases()) {
                inputEditor.setInputs(case)
                val result = when {
                    case.isEmpty() -> case + ("0" + input)
                    case.last().isNumber() && !case.last()
                        .contains(Constants.decimalSymbol) -> case.subList(
                        0,
                        case.lastIndex
                    ) + (case.last() + input)

                    case.last().isNumber() && case.last().contains(Constants.decimalSymbol) -> null

                    case.last() in Constants.arithmeticOperators || case.last()
                        .endsWith(Constants.bracketOpen) -> case + "0" + Constants.decimalSymbol

                    else -> case + listOf<String>(
                        Constants.multiplySymbol,
                        "0" + Constants.decimalSymbol
                    )
                }
                Assert.assertEquals(
                    errorMessages[index],
                    result?.joinToString(""),
                    inputEditor.addInput(input),
                )
                index += 1
            }
        }
    }

    @Test
    fun `Test inputting operator`() {
        val inputs = Constants.arithmeticOperators + Constants.middleOperations
        val errorMessages = getAssertionErrorMessages()
        val inputEditor = InputEditor()
        for (input in inputs) {
            var index = 0
            for (case in getInputEndingCases()) {
                inputEditor.setInputs(case)
                val result = when {
                    case.isEmpty() -> null
                    case.last()
                        .endsWith(Constants.bracketOpen) || case.last() in (Constants.arithmeticOperators + Constants.middleOperations) -> null

                    else -> case + input
                }
                Assert.assertEquals(
                    errorMessages[index],
                    result?.joinToString(""),
                    inputEditor.addInput(input),
                )
                index += 1
            }
        }
    }

    @Test
    fun `Test inputting brackets`() {
        val inputs = listOf<String>(Constants.bracketSymbol)
        val errorMessages = getAssertionErrorMessages()
        val inputEditor = InputEditor()
        for (input in inputs) {
            var index = 0
            for (case in getInputEndingCases()) {
                inputEditor.setInputs(case)
                val result = when {
                    case.isEmpty() -> case + Constants.bracketOpen
                    case.last()
                        .isNumber() || case.last() in Constants.constantSymbols || case.last()
                        .startsWith(Constants.bracketClose) -> if (case.count { el ->
                            el.endsWith(
                                Constants.bracketOpen
                            ) && el != Constants.leftAbsBracket
                        } > case.count { el -> el.startsWith(Constants.bracketClose) && el != Constants.rightAbsBracket }
                    ) case + Constants.bracketClose else case + listOf<String>(
                        Constants.multiplySymbol,
                        Constants.bracketOpen
                    )

                    else -> case + Constants.bracketOpen
                }
                Assert.assertEquals(
                    errorMessages[index],
                    result.joinToString(""),
                    inputEditor.addInput(input),
                )
                index += 1
            }
        }
    }

    @Test
    fun `Test inputting abs brackets`() {
        val inputs = listOf<String>(Constants.absSymbol)
        val errorMessages = getAssertionErrorMessages()
        val inputEditor = InputEditor()
        for (input in inputs) {
            var index = 0
            for (case in getInputEndingCases()) {
                inputEditor.setInputs(case)
                val result = when {
                    case.isEmpty() -> case + Constants.leftAbsBracket
                    case.last()
                        .isNumber() || case.last() in Constants.constantSymbols || case.last()
                        .startsWith(Constants.bracketClose) -> if (case.count { el -> el == Constants.leftAbsBracket } > case.count { el -> el == Constants.rightAbsBracket }
                    ) case + Constants.rightAbsBracket else case + listOf<String>(
                        Constants.multiplySymbol,
                        Constants.leftAbsBracket
                    )

                    else -> case + Constants.leftAbsBracket
                }
                Assert.assertEquals(
                    errorMessages[index],
                    result.joinToString(""),
                    inputEditor.addInput(input),
                )
                index += 1
            }
        }
    }

    @Test
    fun `Test signing last number`() {
        val errorMessages = getAssertionErrorMessages()
        val inputEditor = InputEditor()
        var index = 0
        for (case in getInputEndingCases()) {
            inputEditor.setInputs(case)
            val result = when {
                case.isEmpty() -> null
                case.last()
                    .isNumber() -> if (Constants.negativeSign in case.last()) case.subList(
                    0,
                    case.lastIndex
                ) + case.last().substring(1) else case.subList(
                    0,
                    case.lastIndex
                ) + (Constants.negativeSign + case.last())

                else -> null
            }
            Assert.assertEquals(
                errorMessages[index],
                result?.joinToString(""),
                inputEditor.signLastNumber(),
            )
            index += 1
        }
    }

    @Test
    fun `Test clear function`() {
        val errorMessages = getAssertionErrorMessages()
        val inputEditor = InputEditor()
        var index = 0
        for (case in getInputEndingCases()) {
            inputEditor.setInputs(case)
            Assert.assertEquals(
                errorMessages[index],
                "",
                inputEditor.clear(),
            )
            index += 1
        }
    }

    @Test
    fun `Test backspace`() {
        val errorMessages = getAssertionErrorMessages()
        val inputEditor = InputEditor()
        var index = 0
        for (case in getInputEndingCases()) {
            inputEditor.setInputs(case)
            val result = when {
                case.isEmpty() -> listOf<String>()
                case.last().isNumber() && case.last().replace(
                    Constants.negativeSign,
                    ""
                ).length > 1 -> case.subList(
                    0,
                    case.lastIndex
                ) + case.last().substring(0, case.last().lastIndex)

                else -> case.subList(0, case.lastIndex)
            }
            Assert.assertEquals(
                errorMessages[index],
                result.joinToString(""),
                inputEditor.backspace(),
            )
            index += 1
        }
    }

    // BELOW ARE HELPER METHODS USED IN GENERATING TEST CASES

    private fun getInputEndingCases(): List<MutableList<String>> {
        return listOf<MutableList<String>>(
            getEmptyInputs(),
            getInputsEndingWithPositiveNumber(),
            getInputsEndingWithNegativeNumber(),
            getInputsEndingWithDecimalNumber(),
            getInputsEndingWithConstant(),
            getInputsEndingWithAnOperator(),
            getInputsEndingWithLeftBracket(),
            getInputsEndingWithRightBracket(),
            getInputsEndingWithLeftAbsBracket(),
            getInputsEndingWithRightAbsBracket()
        )
    }

    private fun getAssertionErrorMessages(): List<String> {
        return listOf<String>(
            "Failed test case of empty string",
            "Failed test case ending in a positive number",
            "Failed test case ending in a negative number",
            "Failed test case ending in a decimal number",
            "Failed test case ending in a constant",
            "Failed test case ending in an operator",
            "Failed test case ending in a left bracket",
            "Failed test case ending in a right bracket",
            "Failed test case ending in a left abs bracket",
            "Failed test case ending in a right abs bracket"
        )
    }

    private fun getEmptyInputs(): MutableList<String> {
        return mutableListOf<String>()
    }

    private fun getInputsEndingWithPositiveNumber(): MutableList<String> {
        return mutableListOf("7")
    }

    private fun getInputsEndingWithNegativeNumber(): MutableList<String> {
        return mutableListOf("-7")
    }

    private fun getInputsEndingWithDecimalNumber(): MutableList<String> {
        return mutableListOf("7.0")
    }

    private fun getInputsEndingWithConstant(): MutableList<String> {
        return mutableListOf(Constants.piSymbol)
    }

    private fun getInputsEndingWithLeftBracket(): MutableList<String> {
        return mutableListOf(Constants.bracketOpen)
    }

    private fun getInputsEndingWithRightBracket(): MutableList<String> {
        return mutableListOf(Constants.bracketOpen, "3", Constants.bracketClose)
    }

    private fun getInputsEndingWithLeftAbsBracket(): MutableList<String> {
        return mutableListOf(Constants.leftAbsBracket)
    }

    private fun getInputsEndingWithRightAbsBracket(): MutableList<String> {
        return mutableListOf(Constants.leftAbsBracket, "3", Constants.rightAbsBracket)
    }

    private fun getInputsEndingWithAnOperator(): MutableList<String> {
        return mutableListOf(Constants.bracketOpen, "3", Constants.plusSymbol)
    }
}