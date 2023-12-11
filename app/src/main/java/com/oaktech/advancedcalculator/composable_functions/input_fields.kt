package com.oaktech.advancedcalculator.composable_functions

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oaktech.advancedcalculator.ui.theme.AccentColor
import com.oaktech.advancedcalculator.ui.theme.MainColor
import com.oaktech.advancedcalculator.ui.theme.TextColor

/**
 * Element that creates a TextField used in input/output of calculations
 *
 * @param textValue the text displayed on the TextField
 */
@Composable
fun MainInputField(textValue: TextFieldValue) {
    TextField(
        value = textValue,
        onValueChange = {},
        colors = TextFieldDefaults.colors(
            disabledContainerColor = MainColor,
            disabledTextColor = TextColor
        ),
        enabled = false,
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontSize = 40.sp)
    )
}

/**
 * Element that creates a TextField used when multiple textfields are displayed at a time
 *
 * @param textValue the text displayed on the text field
 * @param highlight whether the user is currently editing this text field
 * @param prefix text displayed at the beginning of the input in the text field
 * @param suffix text displayed at the end of the input in the text field
 * @param modifier
 * @param onClick action to be performed if the text field is clicked
 */
@Composable
fun SecondaryInputField(
    textValue: TextFieldValue,
    highlight: Boolean,
    prefix: String = "",
    suffix: String = "",
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedTextField(
        value = textValue,
        onValueChange = {},
        colors = TextFieldDefaults.colors(
            disabledContainerColor = MainColor,
            disabledTextColor = TextColor
        ),
        enabled = false,
        modifier = modifier.fillMaxWidth().padding(10.dp).border(
            width = if (highlight) 1.dp else Dp.Unspecified,
            color = AccentColor
        ).clickable {
            onClick()
        },
        singleLine = true,
        prefix = {
            Text(prefix)
        },
        suffix = {
            Text(suffix)
        },
    )
}