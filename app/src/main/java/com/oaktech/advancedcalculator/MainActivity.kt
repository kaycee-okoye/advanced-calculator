package com.oaktech.advancedcalculator

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import com.oaktech.advancedcalculator.composable_functions.InputButton
import com.oaktech.advancedcalculator.composable_functions.MainInputField
import com.oaktech.advancedcalculator.composable_functions.SingleButtonAppbar
import com.oaktech.advancedcalculator.controllers.InputEditor
import com.oaktech.advancedcalculator.pages.ModeMenu
import com.oaktech.advancedcalculator.ui.theme.AdvancedCalculatorTheme
import com.oaktech.advancedcalculator.ui.theme.DarkColor
import com.oaktech.advancedcalculator.ui.theme.MainColor

class MainActivity : ComponentActivity() {
    private val inputEditor = InputEditor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenUI()
        }
    }

    /**
     * Element that displays the basic calculator
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
                    SingleButtonAppbar(
                        icon = Icons.Default.Menu,
                        description = "Modes Menu",
                        onClick = {
                            val intent = Intent(this@MainActivity, ModeMenu::class.java)
                            startActivity(intent)
                        },
                        alignment = Alignment.CenterEnd
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        MainInputField(TextFieldValue(input, TextRange(input.length)))
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth().height(5.dp)
                            .background(color = DarkColor)
                    )
                    Box(modifier = Modifier.weight(2f)) {
                        InputKeyboard { newValue -> input = newValue }
                    }
                }
            }
        }
    }

    /**
     * Element that displays in-app input keyboard
     *
     * @param onUpdate action to be performed after the main input field
     * was updated by a button on the input keyboard
     */
    @Composable
    private fun InputKeyboard(onUpdate: (String) -> Unit) {
        val row1 = listOf<String>(
            Constants.clearSymbol, Constants.multiplySymbol,
            Constants.divideSymbol, Constants.deleteSymbol
        )
        val row2 = listOf<String>("7", "8", "9", Constants.plusSymbol)
        val row3 = listOf<String>("4", "5", "6", Constants.minusSymbol)
        val row4 = listOf<String>("1", "2", "3", Constants.bracketSymbol)
        val row5 = listOf<String>(
            "0",
            Constants.decimalSymbol,
            Constants.signSymbol,
            Constants.solveSymbol
        )
        val keys = listOf(row1, row2, row3, row4, row5)
        Box(
            modifier = Modifier.fillMaxHeight().fillMaxWidth()
                .background(color = MainColor)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                keys.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        row.forEach { value ->
                            InputButton(text = value,
                                onClick = {
                                    val newValue = inputButtonPressed(value)
                                    if (newValue == null) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Invalid Input",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        onUpdate(newValue)
                                    }
                                })
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
     *
     * @return the updated text to display on the main input field
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
     * Preview of the Basic calculator
     */
    @Preview(showBackground = true)
    @Composable
    private fun PagePreview() {
        ScreenUI()
    }
}