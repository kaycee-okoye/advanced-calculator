package com.oaktech.advancedcalculator

import com.oaktech.advancedcalculator.controllers.LinearSystemOfEquationsCalculations
import org.junit.Assert
import org.junit.Test

/**
 * Contains Unit Tests that ensure that polynomial roots
 * are calculated accurately
 */
class LinearSystemSolverTests {

    @Test
    fun `Test linear system with two unknowns`() {
        val tests =
            listOf<MutableList<MutableList<String>>>(
                mutableListOf(
                    mutableListOf("1", "1", "3"),
                    mutableListOf("2", "1", "4"),
                ),
                mutableListOf(
                    mutableListOf("1", "1", "3"),
                    mutableListOf("2", "2", "6"),
                ),
                mutableListOf(
                    mutableListOf("1", "1", "3"),
                    mutableListOf("2", "2", "7"),
                ),
            )
        val results = listOf<List<String>?>(
            listOf("1", "2"),
            listOf(Constants.infinitySymbol),
            null
        )
        for (i in tests.indices) {
            val test = tests[i]
            val actual =
                LinearSystemOfEquationsCalculations(coeffs = test).solution()
            assertLists(
                errorMessage = "System of two unknowns failed (${test}): $actual != ${results[i]}",
                expected = results[i],
                actual = actual,
            )
        }
    }

    @Test
    fun `Test linear system with three unknowns`() {
        val tests =
            listOf<MutableList<MutableList<String>>>(
                mutableListOf(
                    mutableListOf("1", "1", "1", "6"),
                    mutableListOf("2", "1", "1", "7"),
                    mutableListOf("1", "2", "1", "8"),
                ),
                mutableListOf(
                    mutableListOf("1", "1", "1", "6"),
                    mutableListOf("2", "3", "2", "14"),
                    mutableListOf("1", "2", "1", "8"),
                ),
                mutableListOf(
                    mutableListOf("1", "1", "1", "6"),
                    mutableListOf("2", "3", "2", "13"),
                    mutableListOf("1", "2", "1", "8"),
                ),
            )
        val results = listOf<List<String>?>(
            listOf("1", "2", "3"),
            listOf(Constants.infinitySymbol),
            null
        )
        for (i in tests.indices) {
            val test = tests[i]
            val actual =
                LinearSystemOfEquationsCalculations(coeffs = test).solution()
            assertLists(
                errorMessage = "System of two unknowns failed (${test}): $actual != ${results[i]}",
                expected = results[i],
                actual = actual,
            )
        }
    }

    @Test
    fun `Test linear system with four unknowns`() {
        val tests =
            listOf<MutableList<MutableList<String>>>(
                mutableListOf(
                    mutableListOf("1", "1", "1", "1", "10"),
                    mutableListOf("2", "1", "1", "1", "11"),
                    mutableListOf("1", "2", "1", "1", "12"),
                    mutableListOf("1", "1", "2", "1", "13"),
                ),
                mutableListOf(
                    mutableListOf("1", "1", "1", "1", "10"),
                    mutableListOf("2", "2", "2", "2", "20"),
                    mutableListOf("1", "2", "1", "1", "12"),
                    mutableListOf("1", "1", "2", "1", "13"),
                ),
                mutableListOf(
                    mutableListOf("1", "1", "1", "1", "10"),
                    mutableListOf("2", "2", "2", "2", "21"),
                    mutableListOf("1", "2", "1", "1", "12"),
                    mutableListOf("1", "1", "2", "1", "13"),
                ),
            )
        val results = listOf<List<String>?>(
            listOf("1", "2", "3", "4"),
            listOf(Constants.infinitySymbol),
            null
        )
        for (i in tests.indices) {
            val test = tests[i]
            val actual =
                LinearSystemOfEquationsCalculations(coeffs = test).solution()
            assertLists(
                errorMessage = "System of two unknowns failed (${test}): $actual != ${results[i]}",
                expected = results[i],
                actual = actual,
            )
        }
    }

    private fun assertLists(
        errorMessage: String,
        expected: List<String>?,
        actual: List<String>?,
    ) {
        Assert.assertEquals(
            errorMessage,
            expected == null,
            actual == null
        )
        if (expected != null && actual != null) {
            for (i in 0..actual.lastIndex) {
                Assert.assertEquals(
                    errorMessage,
                    expected[i],
                    actual[i]
                )
            }
        }
    }
}