package com.oaktech.advancedcalculator.pages

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.oaktech.advancedcalculator.composable_functions.ScientificInputKeyboard
import com.oaktech.advancedcalculator.composable_functions.MainInputField
import com.oaktech.advancedcalculator.composable_functions.SingleButtonAppbar
import com.oaktech.advancedcalculator.controllers.InputEditor
import com.oaktech.advancedcalculator.ui.theme.AdvancedCalculatorTheme
import com.oaktech.advancedcalculator.ui.theme.DarkColor
import com.oaktech.advancedcalculator.ui.theme.MainColor

class ScientificCalculator : AppCompatActivity() {
    private val inputEditor = InputEditor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        setContent {
            ScreenUI()
        }
    }

    /**
     * Element that displays the scientific calculator
     */
    @Composable
    private fun ScreenUI() {
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
                                this@ScientificCalculator.finish()
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
                                context = this@ScientificCalculator,
                                onUpdate = { newValue, _ -> input = newValue },
                                inputButtonPressed = ::inputButtonPressed
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates [MainInputField] when a button on the in-app keyboard is clicked
     *
     * @param value the text displayed on the button that was clicked
     */
    private fun inputButtonPressed(value: String): String? {
        if (value == Constants.clearSymbol) {
            return inputEditor.clear()
        } else if (value == Constants.deleteSymbol) {
            return inputEditor.backspace()
        } else if (value == Constants.signSymbol) {
            return inputEditor.signLastNumber()
        } else if (value == Constants.solveSymbol) {
            return inputEditor.done()
        } else {
            return inputEditor.addInput(value)
        }
    }

    /**
     * Preview of the scientific calculator
     */
    @Preview(showBackground = true)
    @Composable
    private fun PagePreview() {
        AdvancedCalculatorTheme {
            ScreenUI()
        }
    }
}