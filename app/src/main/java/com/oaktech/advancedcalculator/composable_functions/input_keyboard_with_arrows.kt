package com.oaktech.advancedcalculator.composable_functions

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.oaktech.advancedcalculator.Constants
import com.oaktech.advancedcalculator.controllers.SimpleInputEditor
import com.oaktech.advancedcalculator.models.InputValue
import com.oaktech.advancedcalculator.ui.theme.AccentColor
import com.oaktech.advancedcalculator.ui.theme.MainColor


/**
 * Element that displays in-app input keyboard
 *
 * @param context
 * @param inputs the text being displayed on each input field
 * @param currentFieldIndex the index of the input field currently
 * being highlighted
 * @param currentFieldIndexInInputs the index of the content of the input field currently
 * being highlighted, in [inputs]
 * @param minFieldIndex the lowest permissible value of [currentFieldIndexInInputs]
 * @param maxFieldIndex the highest permissible value of [currentFieldIndexInInputs]
 * @param onArrowClick action to be performed when [currentFieldIndexInInputs] is
 * updated
 * @param onClearClick action to be performed when the clear button
 * is clicked
 * @param onUpdate action to be performed after an input field
 * was updated by a button on the input keyboard
 * @param solver([inputs] ,[order]) function that generates to solve
 * the current problem and generate an output message to be displayed
 * in an alert dialog
 */
@Composable
fun InputKeyboardWithArrows(
    context: Context,
    inputs: MutableList<InputValue>,
    currentFieldIndex: Int,
    currentFieldIndexInInputs: Int,
    minFieldIndex: Int,
    maxFieldIndex: Int,
    onArrowClick: (Int) -> Unit,
    onClearClick: () -> Unit,
    onUpdate: (String) -> Unit,
    solver: (MutableList<InputValue>) -> String?
) {
    val row1 = listOf<String>("7", "8", "9", Constants.clearAllSymbol)
    val row2 = listOf<String>("4", "5", "6", Constants.clearSymbol)
    val row3 = listOf<String>("1", "2", "3", Constants.deleteSymbol)
    val row4 = listOf<String>(
        "0",
        Constants.decimalSymbol,
        Constants.signSymbol,
        Constants.solveSymbol
    )
    val keys = listOf(row1, row2, row3, row4)

    var shouldShowDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxHeight().fillMaxWidth()
            .background(color = MainColor)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (shouldShowDialog) {
                showDialog(
                    dialogMessage, onDismiss = {
                        shouldShowDialog = false
                        dialogMessage = ""
                    })
            }

            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InputIconButton(
                    icon = Icons.Default.KeyboardArrowLeft,
                    description = "Previous",
                    onClick = {
                        onArrowClick(if (currentFieldIndex == minFieldIndex) maxFieldIndex else currentFieldIndex - 1)
                    })
                InputIconButton(
                    icon = Icons.Default.KeyboardArrowRight,
                    description = "Next",
                    onClick = {
                        onArrowClick((currentFieldIndex + 1) % (maxFieldIndex + 1))
                    })
            }
            keys.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row.forEach { value ->
                        InputButton(text = value, onClick = {
                            when (value) {
                                Constants.clearAllSymbol -> onClearClick()

                                Constants.solveSymbol -> {
                                    val result = solver(inputs)
                                    if (result != null) {
                                        dialogMessage = result
                                        shouldShowDialog = true
                                    }
                                }

                                else -> {
                                    val newValue = SimpleInputEditor.inputButtonPressed(
                                        inputs[currentFieldIndexInInputs].value,
                                        value
                                    )
                                    if (newValue == null) {
                                        Toast.makeText(
                                            context,
                                            "Invalid Input",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        onUpdate(newValue)
                                    }
                                }
                            }
                        })
                    }
                }
            }
        }
    }
}


/**
 * Element to show simple alert dialog message
 *
 * @param message message to be displayed
 * @param onDismiss action to be performed when user
 * requests the dismissal of the alert dialog
 */
@Composable
private fun showDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        title = { Text(text = "Solution") },
        text = {
            Text(
                text = message,
                textAlign = TextAlign.Center,
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss)
            { Text(text = "Done", color = AccentColor) }
        },
    )
}