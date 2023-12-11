package com.oaktech.advancedcalculator.pages

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oaktech.advancedcalculator.Constants
import com.oaktech.advancedcalculator.composable_functions.AppbarWithEditableOrder
import com.oaktech.advancedcalculator.composable_functions.InputKeyboardWithArrows
import com.oaktech.advancedcalculator.composable_functions.SecondaryInputField
import com.oaktech.advancedcalculator.controllers.LinearSystemOfEquationsCalculations
import com.oaktech.advancedcalculator.extensions.enforceFinalAnswerPrecision
import com.oaktech.advancedcalculator.models.InputValue
import com.oaktech.advancedcalculator.ui.theme.AdvancedCalculatorTheme
import com.oaktech.advancedcalculator.ui.theme.DarkColor
import com.oaktech.advancedcalculator.ui.theme.MainColor
import com.oaktech.advancedcalculator.ui.theme.TextColor

class LinearSystemOfEquationsSolver : AppCompatActivity() {
    private val maxOrder = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenUI()
        }
    }

    /**
     * Element that displays the polynomial root finder
     */
    @Composable
    private fun ScreenUI() {
        var order by remember { mutableStateOf(2) }
        var fieldPointer by remember { mutableStateOf(0) }
        var currentField by remember { mutableStateOf(0) }
        val inputs = remember {
            mutableStateListOf(
                InputValue(""),
                InputValue(""),
                InputValue(""),
                InputValue(""),
                InputValue(""),

                InputValue(""),
                InputValue(""),
                InputValue(""),
                InputValue(""),
                InputValue(""),

                InputValue(""),
                InputValue(""),
                InputValue(""),
                InputValue(""),
                InputValue(""),

                InputValue(""),
                InputValue(""),
                InputValue(""),
                InputValue(""),
                InputValue(""),
            )
        }
        AdvancedCalculatorTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MainColor
            ) {
                Column {
                    AppbarWithEditableOrder(
                        currentOrder = order,
                        onBack = {
                            this@LinearSystemOfEquationsSolver.finish()
                        },
                        onReduce = {
                            if (order > 1) {
                                order -= 1
                                fieldPointer = Math.min((order * order) + order - 1, fieldPointer)
                                currentField =
                                    ((fieldPointer / (order + 1)) * (maxOrder + 1)) + (fieldPointer % (order + 1))
                            }
                        },
                        onIncrease = {
                            if (order < maxOrder) {
                                order += 1
                            }
                        }
                    )
                    Box(modifier = Modifier.weight(1f).background(MainColor)) {
                        InputFields(
                            inputs = inputs,
                            order = order,
                            currentField = currentField,
                            onFieldClick = { newField ->
                                run {
                                    currentField = newField
                                    fieldPointer =
                                        ((newField / (maxOrder + 1)) * (order + 1)) + (newField % (maxOrder + 1))
                                }
                            })
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth().height(5.dp)
                            .background(color = DarkColor)
                    )
                    Box(modifier = Modifier.weight(if (order < 4) 2f else 1f)) {
                        InputKeyboardWithArrows(
                            context = this@LinearSystemOfEquationsSolver,
                            inputs = inputs,
                            minFieldIndex = 0,
                            maxFieldIndex = (order * order) + order - 1,
                            currentFieldIndex = fieldPointer,
                            currentFieldIndexInInputs = currentField,
                            onArrowClick = { newField ->
                                run {
                                    fieldPointer = newField
                                    currentField =
                                        ((newField / (order + 1)) * (maxOrder + 1)) + (newField % (order + 1))
                                }
                            },
                            onClearClick = {
                                for (i in 0..inputs.lastIndex) {
                                    inputs[i] = InputValue("")
                                }
                            },
                            onUpdate = { newValue -> inputs[currentField] = InputValue(newValue) },
                            solver = {solve(it, order)}
                        )
                    }
                }
            }
        }
    }

    /**
     * Displays appropriate number of input fields for current [order]
     *
     * @param inputs the text to be displayed on each input field
     * @param order the number of variables. This determines the
     * number of required text fields
     * @param currentField the index of the input field to highlight
     * @param onFieldClick action to be performed when a text field is clicked
     */
    @Composable
    private fun InputFields(
        inputs: MutableList<InputValue>,
        order: Int,
        currentField: Int,
        onFieldClick: (Int) -> Unit
    ) {
        Column {
            for (i in 0..order - 1) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    for (j in 0..order) {
                        SecondaryInputField(
                            textValue = TextFieldValue(
                                inputs[((i * (maxOrder + 1)) + j)].value,
                                TextRange(inputs[j].value.length)
                            ),
                            highlight = ((i * (maxOrder + 1)) + j) == currentField,
                            suffix = if (j % (order + 1) != order && order < 4) "x${j + 1}" else "",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onFieldClick((i * (maxOrder + 1)) + j)
                            }
                        )
                        if (j % (order + 1) != order) {
                            Text(
                                text = if (j % (order + 1) != order - 1) Constants.plusSymbol else Constants.solveSymbol,
                                color = TextColor,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Calculates the solution to the linear system of equations
     *
     * @param inputs the text currently being displayed in the input fields.
     * These represent the coefficients of the polynomial
     * @param order the number of variables in the linear system of equations
     */
    private fun solve(inputs: MutableList<InputValue>, order: Int): String? {
        val coeffs = mutableListOf<MutableList<String>>()
        for (i in 0..order - 1) {
            val coeffsI = mutableListOf<String>()
            for (j in 0..order) {
                val coeff = inputs[((i * (maxOrder + 1)) + j)].value
                if (coeff.isBlank()) {
                    Toast.makeText(
                        this@LinearSystemOfEquationsSolver,
                        "Please provide coefficient for ${if (i > 1) "x^$i" else if (i == 1) "x" else "d"} in equation ${i + 1}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return null
                } else {
                    coeffsI.add(coeff)
                }
            }
            coeffs.add(coeffsI)
        }
        val solutions = LinearSystemOfEquationsCalculations(coeffs).solution()
        return when {
            solutions == null -> "No solution exists"
            solutions.contains(Constants.infinitySymbol) -> "There are infinite solutions"
            else -> "The solutions are: \n\n${
                solutions.mapIndexed { index, it -> "x${index + 1} = ${it.enforceFinalAnswerPrecision()}" }
                    .joinToString(" , ")
            }"
        }
    }

    /**
     * Preview of the Equation solver
     */
    @Preview(showBackground = true)
    @Composable
    private fun PagePreview() {
        ScreenUI()
    }
}