package com.oaktech.advancedcalculator

import com.oaktech.advancedcalculator.controllers.InputEditor
import org.junit.Assert
import org.junit.Test

/**
 * Contains Unit Tests that ensure that all math functions
 * are functioning as expected
 */
class FunctionTests {
    @Test
    fun `Test left bracket operations`() {
        val inputNumbers = listOf<String>(
            "9",
            "10",
            "30",
            "60",
            "45",
            "1",
            "1",
            "1",
            "10",
            Constants.eulerSymbol,
            Math.log(10.0).toString()
        )
        val tests = Constants.leftBracketOperations.map { operator ->
            mutableListOf(
                operator + Constants.bracketOpen,
                inputNumbers[Constants.leftBracketOperations.indexOf(operator)],
                Constants.bracketClose
            )
        }
        val results =
            listOf<String>("3", "0.1", "0.5", "0.5", "1", "90", "0", "45", "1", "1", "10")
        for (i in 0..tests.size - 1) {
            val inputEditor = InputEditor()
            inputEditor.setInputs(tests[i])
            Assert.assertEquals(
                "Left bracket operator failed: ${tests[i]}",
                results[i],
                inputEditor.done(),
            )
        }
    }

    @Test
    fun `Test trig operations in radians`() {
        val tests = listOf<String>(
            Constants.sinSymbol,
            Constants.cosSymbol,
            Constants.tanSymbol
        ).map { trigFunction ->
            mutableListOf(
                trigFunction + Constants.bracketOpen,
                Constants.piSymbol,
                Constants.bracketClose
            )
        }
        val results =
            listOf<String>("0", "-1", "0")
        for (i in 0..tests.size - 1) {
            val inputEditor = InputEditor()
            inputEditor.setInputs(tests[i])
            Assert.assertEquals(
                "Left bracket operator failed: ${tests[i]}",
                results[i],
                inputEditor.done(),
            )
        }
    }

    @Test
    fun `Test non-arithmetic middle operators`() {
        val tests =
            Constants.middleOperations.map { operator -> mutableListOf("4", operator, "2") }
        val results = listOf<String>("12", "6", "16")
        for (i in 0..tests.size - 1) {
            val inputEditor = InputEditor()
            inputEditor.setInputs(tests[i])
            Assert.assertEquals(
                "Middle non-arithmetic operator failed: ${tests[i]}",
                results[i],
                inputEditor.done(),
            )
        }
    }

    @Test
    fun `Test right bracket operations`() {
        val tests = Constants.rightBracketOperations.map { operator ->
            mutableListOf(
                Constants.bracketOpen,
                "5",
                Constants.bracketClose + operator
            )
        }
        val results =
            listOf<String>("25", "120", "0.05")
        for (i in 0..tests.size - 1) {
            val inputEditor = InputEditor()
            inputEditor.setInputs(tests[i])
            Assert.assertEquals(
                "Right bracket operator failed: ${tests[i]}",
                results[i],
                inputEditor.done(),
            )
        }
    }

    @Test
    fun `Test non arithmetic operator error cases`() {
        val tests = listOf<MutableList<String>>(
            // -1!
            mutableListOf(
                Constants.bracketOpen,
                "-1",
                Constants.bracketClose+Constants.facSymbol,
            ),
            // 0^-5
            mutableListOf(
                "0",
                Constants.powerSymbol,
                "-5",
            ),
            // sqrt(-1)
            mutableListOf(
                Constants.sqrtSymbol+Constants.bracketOpen,
                "-10",
                Constants.bracketClose
            ),
            // 0^-1
            mutableListOf(
                Constants.invSymbol+Constants.bracketOpen,
                "0",
                Constants.bracketClose
            ),
            // tan(90)
            mutableListOf(
                Constants.tanSymbol+Constants.bracketOpen,
                "90",
                Constants.bracketClose
            ),
            // log(-1)
            mutableListOf(
                Constants.logSymbol+Constants.bracketOpen,
                "-1",
                Constants.bracketClose
            ),
            // ln(-1)
            mutableListOf(
                Constants.lnSymbol+Constants.bracketOpen,
                "-1",
                Constants.bracketClose
            ),
        )
        for (i in 0..tests.size - 1) {
            val inputEditor = InputEditor()
            inputEditor.setInputs(tests[i])
            Assert.assertEquals(
                "Non arithmetic operator error case test failed: ${tests[i]}",
                Constants.errorSymbol,
                inputEditor.done(),
            )
        }
    }
}