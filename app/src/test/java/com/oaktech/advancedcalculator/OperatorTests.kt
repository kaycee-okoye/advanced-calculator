package com.oaktech.advancedcalculator

import com.oaktech.advancedcalculator.controllers.InputEditor
import org.junit.Assert
import org.junit.Test

/**
 * Contains Unit Tests that ensure that all operators
 * are functioning as expected
 */
class OperatorTests {
    @Test
    fun `Test arithmetic operators`() {
        val tests =
            Constants.arithmeticOperators.map { operator -> mutableListOf("5", operator, "5") }
        val results = listOf<String>("1", "25", "10", "0")
        for (i in 0..tests.size - 1) {
            val inputEditor = InputEditor()
            inputEditor.setInputs(tests[i])
            Assert.assertEquals(
                "Operator failed: ${tests[i]}",
                results[i],
                inputEditor.done(),
            )
        }
    }

    @Test
    fun `Test arithmetic inputs containing brackets`() {
        val tests = listOf<MutableList<String>>(
            // (5 + 5)
            mutableListOf(
                Constants.bracketOpen,
                "5",
                Constants.plusSymbol,
                "5",
                Constants.bracketClose
            ),
            // (5 + 5) + (5 + 5)
            mutableListOf(
                Constants.bracketOpen,
                "5",
                Constants.plusSymbol,
                "5",
                Constants.bracketClose,
                Constants.plusSymbol,
                Constants.bracketOpen,
                "5",
                Constants.plusSymbol,
                "5",
                Constants.bracketClose
            ),
            // ((5 + 5) + (5 + 5)) * 5
            mutableListOf(
                Constants.bracketOpen,
                Constants.bracketOpen,
                "5",
                Constants.plusSymbol,
                "5",
                Constants.bracketClose,
                Constants.plusSymbol,
                Constants.bracketOpen,
                "5",
                Constants.plusSymbol,
                "5",
                Constants.bracketClose,
                Constants.bracketClose,
                Constants.multiplySymbol,
                "5"
            ),
        )
        val results = listOf<String>("10", "20", "100")
        for (i in 0..tests.size - 1) {
            val inputEditor = InputEditor()
            inputEditor.setInputs(tests[i])
            Assert.assertEquals(
                "Bracket test failed ${tests[i]}",
                results[i],
                inputEditor.done(),
            )
        }
    }

    @Test
    fun `Test the arithmetic operator order of precedence`() {
        val tests = listOf<MutableList<String>>(
            // (5 + 5) - 5 + 5 * 5 / 5 + 5 - (5 + 5)
            mutableListOf(
                Constants.bracketOpen,
                "5",
                Constants.plusSymbol,
                "5",
                Constants.bracketClose,
                Constants.minusSymbol,
                "5",
                Constants.plusSymbol,
                "5",
                Constants.multiplySymbol,
                "5",
                Constants.divideSymbol,
                "5",
                Constants.plusSymbol,
                "5",
                Constants.minusSymbol,
                Constants.bracketOpen,
                "5",
                Constants.plusSymbol,
                "5",
                Constants.bracketClose,
            ),
        )
        val results = listOf<String>("5")
        for (i in 0..tests.size - 1) {
            val inputEditor = InputEditor()
            inputEditor.setInputs(tests[i])
            Assert.assertEquals(
                "Order of precedence test failed ${tests[i]}",
                results[i],
                inputEditor.done(),
            )
        }
    }

    @Test
    fun `Test arithmetic operator error cases`() {
        val tests = listOf<MutableList<String>>(
            // 1 / 0
            mutableListOf(
                "1",
                Constants.divideSymbol,
                "0",
            ),
        )
        for (i in 0..tests.size - 1) {
            val inputEditor = InputEditor()
            inputEditor.setInputs(tests[i])
            Assert.assertEquals(
                "Arithmetic operator error case test failed: ${tests[i]}",
                Constants.errorSymbol,
                inputEditor.done(),
            )
        }
    }
}