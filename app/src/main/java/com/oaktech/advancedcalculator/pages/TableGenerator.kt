package com.oaktech.advancedcalculator.pages

import android.content.pm.ActivityInfo
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oaktech.advancedcalculator.Constants
import com.oaktech.advancedcalculator.composable_functions.AvailableKeys
import com.oaktech.advancedcalculator.composable_functions.ScientificInputKeyboard
import com.oaktech.advancedcalculator.composable_functions.MainInputField
import com.oaktech.advancedcalculator.composable_functions.SecondaryInputField
import com.oaktech.advancedcalculator.composable_functions.SingleButtonAppbar
import com.oaktech.advancedcalculator.controllers.InputEditor
import com.oaktech.advancedcalculator.controllers.SimpleInputEditor
import com.oaktech.advancedcalculator.extensions.isNumber
import com.oaktech.advancedcalculator.extensions.toFinalAnswerPrecisionString
import com.oaktech.advancedcalculator.extensions.toPreciseBigDecimal
import com.oaktech.advancedcalculator.models.InputValue
import com.oaktech.advancedcalculator.ui.theme.AdvancedCalculatorTheme
import com.oaktech.advancedcalculator.ui.theme.DarkColor
import com.oaktech.advancedcalculator.ui.theme.MainColor
import com.oaktech.advancedcalculator.ui.theme.TextColor
import java.math.BigDecimal
import java.math.RoundingMode

class TableGenerator : AppCompatActivity() {
    private val inputEditor = InputEditor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        setContent {
            var currentPage by remember { mutableStateOf(0) }
            var function by remember { mutableStateOf("") }
            var startValue by remember { mutableStateOf("") }
            var endValue by remember { mutableStateOf("") }
            var stepValue by remember { mutableStateOf("") }
            AdvancedCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MainColor
                ) {
                    if (currentPage == 0) {
                        ScreenUI(onDone = {
                            currentPage = 1
                            function = inputEditor.getInputs().joinToString(" ")
                        })
                    } else if (currentPage == 1) {
                        RangeSelector(
                            onBack = { currentPage = 0; inputEditor.clear() }
                        ) { start, end, step ->
                            run {
                                currentPage = 2
                                startValue = start
                                endValue = end
                                stepValue = step
                            }
                        }
                    } else {
                        TableUI(
                            inputs = function.split(" ").toMutableList(),
                            startValue = startValue.toPreciseBigDecimal(),
                            endValue = endValue.toPreciseBigDecimal(),
                            stepSize = stepValue.toPreciseBigDecimal(),
                            onBack = {
                                currentPage = 1
                                startValue = ""
                                endValue = ""
                                stepValue = ""
                            }
                        )
                    }
                }
            }
        }
    }

    /**
     * Element that displays the scientific calculator
     *
     * @param onDone action performed when user submits a function
     */
    @Composable
    private fun ScreenUI(onDone: () -> Unit) {
        var input by remember { mutableStateOf("") }
        AdvancedCalculatorTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MainColor
            ) {
                Column {
                    Column {
                        SingleButtonAppbar(
                            icon = Icons.Default.ArrowBack,
                            description = "Back",
                            onClick = {
                                this@TableGenerator.finish()
                            },
                            alignment = Alignment.CenterStart
                        )
                        Box(modifier = Modifier.weight(1f)) {
                            MainInputField(TextFieldValue(input, TextRange(input.length)))
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth().height(2.dp)
                                .background(color = DarkColor)
                        )
                        Box(modifier = Modifier.weight(3f)) {
                            ScientificInputKeyboard(
                                context = this@TableGenerator,
                                includeIndependentVariable = true,
                                onUpdate = { newValue, buttonValue ->
                                    run {
                                        input = newValue
                                        if (buttonValue == Constants.solveSymbol) {
                                            onDone()
                                        }
                                    }
                                },
                                inputButtonPressed = { value ->
                                    when (value) {
                                        Constants.clearSymbol -> inputEditor.clear()
                                        Constants.deleteSymbol -> inputEditor.backspace()
                                        Constants.signSymbol -> inputEditor.signLastNumber()
                                        Constants.solveSymbol -> inputEditor.done()
                                        else -> inputEditor.addInput(value)
                                    }
                                })
                        }
                    }
                }
            }
        }
    }

    /**
     * Element that allows the user select a range of values to include in the table
     *
     * @param onBack action performed when the back button is pressed
     * @param onDone (start, stop, end) action performed when user submits a range
     */
    @Composable
    private fun RangeSelector(onBack: () -> Unit, onDone: (String, String, String) -> Unit) {
        var currentField by remember { mutableStateOf(0) }
        val inputs = remember {
            mutableStateListOf(
                InputValue(""),
                InputValue(""),
                InputValue(""),
            )
        }
        Column {
            SingleButtonAppbar(
                icon = Icons.Default.ArrowBack,
                description = "Back",
                onClick = onBack,
                alignment = Alignment.CenterStart
            )

            Box(modifier = Modifier.weight(1f).background(MainColor)) {
                RangeInputFields(
                    inputs = inputs,
                    currentField = currentField,
                    onFieldClick = { newField -> currentField = newField })
            }
            Box(
                modifier = Modifier.fillMaxWidth().height(2.dp)
                    .background(color = DarkColor)
            )
            Box(modifier = Modifier.weight(3f)) {
                ScientificInputKeyboard(
                    context = this@TableGenerator,
                    includeIndependentVariable = true,
                    availableKeys = AvailableKeys.NUMBERS_AND_DECIMALS,
                    onUpdate = { newValue, buttonValue ->
                        run {
                            if (buttonValue == Constants.solveSymbol) {
                                inputs.forEach {
                                    if (it.value.isBlank() || !it.value.isNumber()) {
                                        Toast.makeText(
                                            this@TableGenerator,
                                            "Please fill all fields",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@run
                                    }
                                }
                                val start = inputs[0].value
                                val end = inputs[1].value
                                val step = inputs[2].value
                                if (start.toPreciseBigDecimal() >= end.toPreciseBigDecimal()) {
                                    Toast.makeText(
                                        this@TableGenerator,
                                        "The start value should be less than the end value",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@run
                                }
                                if (step.toPreciseBigDecimal().signum() <= 0) {
                                    Toast.makeText(
                                        this@TableGenerator,
                                        "Step size must be positive",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@run
                                }
                                onDone(start, end, step)
                            } else {
                                inputs[currentField] = InputValue(newValue)
                            }
                        }
                    },
                    inputButtonPressed = { value ->
                        SimpleInputEditor.inputButtonPressed(
                            inputs[currentField].value,
                            value
                        )
                    }
                )
            }
        }
    }

    /**
     * Element that displays a table of values of the provided equation in a given range
     *
     * @param inputs the equation being solved
     * @param startValue the first value in the range
     * @param stepSize the step size of the range
     * @param endValue the last value in the range
     * @param onBack action performed when the back button is pressed
     */
    @Composable
    private fun TableUI(
        inputs: MutableList<String>,
        startValue: BigDecimal,
        stepSize: BigDecimal,
        endValue: BigDecimal,
        onBack: () -> Unit
    ) {
        val count =
            ((endValue - startValue) / stepSize).setScale(0, RoundingMode.FLOOR).toInt() + 1
        Column {
            SingleButtonAppbar(
                icon = Icons.Default.ArrowBack,
                description = "Back",
                onClick = onBack,
                alignment = Alignment.CenterStart
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf<String>("x", "f(x)").forEach {
                    Text(
                        text = it,
                        color = TextColor,
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth().height(2.dp)
                    .background(color = DarkColor)
            )
            LazyColumn {
                items(count) {
                    val localInputEditor = InputEditor()
                    localInputEditor.setInputs(inputs.toMutableList())
                    val value = startValue + (it.toPreciseBigDecimal() * stepSize)
                    val result = localInputEditor.done(value.toPlainString())
                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf<String>(
                            value.toFinalAnswerPrecisionString(),
                            result ?: Constants.errorSymbol
                        ).forEach { value ->
                            Text(
                                text = value,
                                color = TextColor,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Displays appropriate number of input fields for current [order]
     *
     * @param inputs the text to be displayed on each input field
     * @param currentField the index of the input field to highlight
     * @param onFieldClick action to be performed when a text field is clicked
     */
    @Composable
    private fun RangeInputFields(
        inputs: MutableList<InputValue>,
        currentField: Int,
        onFieldClick: (Int) -> Unit
    ) {
        val headers = listOf<String>("Start", "Stop", "Step")
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            for (i in 0..2) {
                SecondaryInputField(
                    textValue = TextFieldValue(
                        inputs[i].value,
                        TextRange(inputs[i].value.length)
                    ),
                    highlight = i == currentField,
                    prefix = headers[i],
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onFieldClick(i)
                    }
                )
            }
        }
    }

    /**
     * Preview of the scientific calculator
     */
    @Preview(showBackground = true)
    @Composable
    private fun PagePreview() {
        AdvancedCalculatorTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MainColor
            ) {
                ScreenUI(onDone = {})
            }
        }
    }
}