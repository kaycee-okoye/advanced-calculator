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
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oaktech.advancedcalculator.Constants
import com.oaktech.advancedcalculator.ui.theme.DarkColor
import com.oaktech.advancedcalculator.ui.theme.MainColor


/**
 * Element that displays in-app input keyboard
 *
 * @param onUpdate (newValue, buttonValue) action to be performed after the main input field
 * was updated by a button on the input keyboard
 * @param inputButtonPressed action to be performed when a button is
 * pressed
 * @param availableKeys enum describing the keys to enable
 */
@Composable
fun ScientificInputKeyboard(
    context: Context,
    onUpdate: (String, String) -> Unit,
    inputButtonPressed: (String) -> String?,
    includeIndependentVariable: Boolean = false,
    availableKeys: AvailableKeys = AvailableKeys.ALL
) {
    val row1_left = listOf<String>(
        Constants.sqrtSymbol, Constants.invSymbol,
        Constants.squareSymbol, Constants.powerSymbol
    )
    val row2_left = listOf<String>(
        Constants.sinSymbol, Constants.cosSymbol,
        Constants.tanSymbol, Constants.piSymbol
    )
    val row3_left = listOf<String>(
        Constants.invSinSymbol, Constants.invCosSymbol,
        Constants.invTanSymbol, Constants.eulerSymbol
    )
    val row4_left = listOf<String>(
        Constants.logSymbol,
        Constants.lnSymbol,
        Constants.expSymbol,
        Constants.absSymbol
    )
    val row5_left = when (includeIndependentVariable) {
        false -> listOf<String>(
            Constants.facSymbol, Constants.percentSymbol,
            Constants.permSymbol, Constants.combSymbol
        )

        true -> listOf<String>(
            Constants.facSymbol, Constants.percentSymbol,
            Constants.independentVariable, ""
        )
    }
    val keys_left = listOf(row1_left, row2_left, row3_left, row4_left, row5_left)

    val row1_right = listOf<String>(
        Constants.clearSymbol, Constants.multiplySymbol,
        Constants.divideSymbol, Constants.deleteSymbol
    )
    val row2_right = listOf<String>("7", "8", "9", Constants.plusSymbol)
    val row3_right = listOf<String>("4", "5", "6", Constants.minusSymbol)
    val row4_right = listOf<String>("1", "2", "3", Constants.bracketSymbol)
    val row5_right = listOf<String>(
        "0",
        Constants.decimalSymbol,
        Constants.signSymbol,
        Constants.solveSymbol
    )
    val keys_right = listOf(row1_right, row2_right, row3_right, row4_right, row5_right)

    Box(
        modifier = Modifier.fillMaxHeight().fillMaxWidth()
            .background(color = MainColor)
    ) {
        Row() {
            Column(
                modifier = Modifier.fillMaxHeight().weight(1f),
            ) {
                keys_left.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        row.forEach { value ->
                            InputButton(
                                text = value,
                                enabled = availableKeys == AvailableKeys.ALL,
                                onClick = {
                                    if (value.isNotBlank()) {
                                        val newValue = inputButtonPressed(value)
                                        if (newValue == null) {
                                            Toast.makeText(
                                                context,
                                                "Invalid Input",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            onUpdate(newValue, value)
                                        }
                                    }
                                })
                        }
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxHeight().width(5.dp)
                    .background(color = DarkColor)
            )

            Column(
                modifier = Modifier.fillMaxHeight().weight(1f),
            ) {
                keys_right.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        row.forEach { value ->
                            InputButton(
                                text = value,
                                enabled = availableKeys == AvailableKeys.ALL || !(value in Constants.arithmeticOperators + Constants.bracketSymbol),
                                onClick = {
                                    val newValue = inputButtonPressed(value)
                                    if (newValue == null) {
                                        Toast.makeText(
                                            context,
                                            "Invalid Input",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        onUpdate(newValue, value)
                                    }
                                })
                        }
                    }
                }
            }
        }
    }
}

enum class AvailableKeys {
    ALL,
    NUMBERS_AND_DECIMALS
}