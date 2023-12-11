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
import com.oaktech.advancedcalculator.controllers.PolynomialRootCalculations
import com.oaktech.advancedcalculator.models.InputValue
import com.oaktech.advancedcalculator.ui.theme.AdvancedCalculatorTheme
import com.oaktech.advancedcalculator.ui.theme.DarkColor
import com.oaktech.advancedcalculator.ui.theme.MainColor
import com.oaktech.advancedcalculator.ui.theme.TextColor
import java.lang.Math.min


class PolynomialRootFinder : AppCompatActivity() {

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
        var currentField by remember { mutableStateOf(2) }
        val inputs = remember {
            mutableStateListOf(
                InputValue(""),
                InputValue(""),
                InputValue(""),
                InputValue("")
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
                            this@PolynomialRootFinder.finish()
                        },
                        onReduce = {
                            if (order > 1) {
                                order -= 1
                                currentField = min(order, currentField)
                                fieldPointer = order - currentField
                            }
                        },
                        onIncrease = {
                            if (order < 3) {
                                order += 1
                                fieldPointer = order - currentField
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
                                    fieldPointer = order - newField
                                }
                            })
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth().height(5.dp)
                            .background(color = DarkColor)
                    )
                    Box(modifier = Modifier.weight(2f)) {
                        InputKeyboardWithArrows(
                            context = this@PolynomialRootFinder,
                            inputs = inputs,
                            minFieldIndex = 0,
                            maxFieldIndex = order,
                            currentFieldIndex = fieldPointer,
                            currentFieldIndexInInputs = currentField,
                            onArrowClick = { newField ->
                                run {
                                    fieldPointer = newField
                                    currentField = order - newField
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
     * @param order the order of the polynomial. This determines the
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
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            for (i in order downTo 0) {
                SecondaryInputField(
                    textValue = TextFieldValue(inputs[i].value, TextRange(inputs[i].value.length)),
                    highlight = i == currentField,
                    suffix = if (i > 1) "x^${i}" else if (i == 1) "x" else "",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onFieldClick(i)
                    }
                )
                if (i != 0) {
                    Text(
                        text = Constants.plusSymbol,
                        color = TextColor,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }

    }

    /**
     * Calculates the roots of the polynomial
     *
     * @param inputs the text currently being displayed in the input fields.
     * These represent the coefficients of the polynomial
     * @param order the order of the polynomial and the number of roots
     * to find
     */
    private fun solve(inputs: MutableList<InputValue>, order: Int): String? {
        for (i in 0..order) {
            if (inputs[i].value.isBlank()) {
                Toast.makeText(
                    this@PolynomialRootFinder,
                    "Please provide coefficient for ${if (i > 1) "x^$i" else if (i == 1) "x" else "d"}",
                    Toast.LENGTH_SHORT
                ).show()
                return null
            }
        }
        val roots =
            PolynomialRootCalculations.findRoots(coeffs = inputs.map { it.value }, order = order)
        return when {
            roots.first() == null -> "Unable to find roots"
            else -> "The root(s) of the polynomial are: \n\n${roots.joinToString(" , ")}"
        }
    }

    /**
     * Preview of the Polynomial root finder
     */
    @Preview(showBackground = true)
    @Composable
    private fun PagePreview() {
        ScreenUI()
    }
}