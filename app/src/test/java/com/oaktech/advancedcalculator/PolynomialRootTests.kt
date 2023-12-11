package com.oaktech.advancedcalculator

import com.oaktech.advancedcalculator.controllers.PolynomialRootCalculations
import org.junit.Assert
import org.junit.Test

/**
 * Contains Unit Tests that ensure that polynomial roots
 * are calculated accurately
 */
class PolynomialRootTests {
    @Test
    fun `Test linear polynomial`() {
        val tests = listOf<List<String>>(listOf("0", "1"), listOf("1", "0"), listOf("4", "2"))
        val results = listOf<List<String?>>(listOf("0"), listOf(null), listOf("-2"))
        for (i in 0..tests.size - 1) {
            val test = tests[i]
            val actual =
                PolynomialRootCalculations.rootOfLinearEquation(order0 = test[0], order1 = test[1])
                    .map { element ->
                        element?.toString()
                    }
            assertLists(
                errorMessage = "Linear polynomial failed (${test}): $actual != ${results[i]}",
                expected = results[i],
                actual = actual,
            )
        }
    }

    @Test
    fun `Test quadratic polynomial`() {
        val tests =
            listOf<List<String>>(
                listOf("1", "2", "1"),
                listOf("2", "2", "1"),
                listOf("-2", "1", "1"),
                listOf("0", "1", "0"),
                listOf("1", "0", "0"),
                listOf("4", "2", "0")
            )
        val results = listOf<List<String?>>(
            listOf("-1", "-1"),
            listOf(
                "-1 ${Constants.plusSymbol} ${Constants.imaginarySymbol}",
                "-1 ${Constants.minusSymbol} ${Constants.imaginarySymbol}"
            ),
            listOf("1", "-2"),
            listOf("0"),
            listOf(null),
            listOf("-2")
        )
        for (i in 0..tests.size - 1) {
            val test = tests[i]
            val actual =
                PolynomialRootCalculations.rootOfQuadraticEquation(
                    order0 = test[0],
                    order1 = test[1],
                    order2 = test[2]
                )
                    .map { element ->
                        element?.toString()
                    }
            assertLists(
                errorMessage = "Quadratic polynomial failed (${test}): $actual != ${results[i]}",
                expected = results[i],
                actual = actual,
            )
        }
    }

    @Test
    fun `Test cubic polynomial`() {
        val tests =
            listOf<List<String>>(
                listOf("-1", "3", "-3", "1"),
                listOf("-4", "8", "-5", "1"),
                listOf("1", "2", "2", "1"),
                listOf("-6", "11", "-6", "1"),
                listOf("1", "2", "1", "0"),
                listOf("2", "2", "1", "0"),
                listOf("-2", "1", "1", "0"),
                listOf("0", "1", "0", "0"),
                listOf("1", "0", "0", "0"),
                listOf("4", "2", "0", "0")
            )
        val results = listOf<List<String?>>(
            listOf("1", "1", "1"),
            listOf("1", "2", "2"),
            listOf(
                "-1",
                "-0.5 ${Constants.plusSymbol} 0.86603${Constants.imaginarySymbol}",
                "-0.5 ${Constants.minusSymbol} 0.86603${Constants.imaginarySymbol}"
            ),
            listOf("1", "2", "3"),
            listOf("-1", "-1"),
            listOf(
                "-1 ${Constants.plusSymbol} ${Constants.imaginarySymbol}",
                "-1 ${Constants.minusSymbol} ${Constants.imaginarySymbol}"
            ),
            listOf("1", "-2"),
            listOf("0"),
            listOf(null),
            listOf("-2")
        )
        for (i in 0..tests.size - 1) {
            val test = tests[i]
            val actual =
                PolynomialRootCalculations.rootOfCubicEquation(
                    order0 = test[0],
                    order1 = test[1],
                    order2 = test[2],
                    order3 = test[3]
                )
                    .map { element ->
                        element?.toString()
                    }
            assertLists(
                errorMessage = "Cubic polynomial failed (${test}): $actual != ${results[i]}",
                expected = results[i],
                actual = actual,
            )
        }
    }

    @Test
    fun `Test general root finder`() {
        val tests =
            listOf<List<String>>(
                listOf("-1", "3", "-3", "1"),
                listOf("-4", "8", "-5", "1"),
                listOf("1", "2", "2", "1"),
                listOf("-6", "11", "-6", "1"),
                listOf("1", "2", "1"),
                listOf("2", "2", "1"),
                listOf("-2", "1", "1"),
                listOf("0", "1"),
                listOf("1", "0"),
                listOf("4", "2")
            )
        val results = listOf<List<String?>>(
            listOf("1", "1", "1"),
            listOf("1", "2", "2"),
            listOf(
                "-1",
                "-0.5 ${Constants.plusSymbol} 0.86603${Constants.imaginarySymbol}",
                "-0.5 ${Constants.minusSymbol} 0.86603${Constants.imaginarySymbol}"
            ),
            listOf("1", "2", "3"),
            listOf("-1", "-1"),
            listOf(
                "-1 ${Constants.plusSymbol} ${Constants.imaginarySymbol}",
                "-1 ${Constants.minusSymbol} ${Constants.imaginarySymbol}"
            ),
            listOf("1", "-2"),
            listOf("0"),
            listOf(null),
            listOf("-2")
        )
        for (i in 0..tests.size - 1) {
            val test = tests[i]
            val actual =
                PolynomialRootCalculations.findRoots(coeffs = test, order = test.size - 1)
                    .map { element ->
                        element?.toString()
                    }
            assertLists(
                errorMessage = "General polynomial failed (${test}): $actual != ${results[i]}",
                expected = results[i],
                actual = actual,
            )
        }
    }

    private fun assertLists(
        errorMessage: String,
        expected: List<String?>,
        actual: List<String?>,
    ) {
        val sortedExpected = expected.sortedWith(compareBy(nullsLast<String>()) { it })
        val sortedActual = actual.sortedWith(compareBy(nullsLast<String>()) { it })
        for (i in 0..actual.lastIndex) {
            Assert.assertEquals(
                errorMessage,
                sortedExpected[i],
                sortedActual[i]
            )
        }
    }
}