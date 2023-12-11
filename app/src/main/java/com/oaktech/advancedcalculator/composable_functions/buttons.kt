package com.oaktech.advancedcalculator.composable_functions

import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.oaktech.advancedcalculator.ui.theme.MainColor
import com.oaktech.advancedcalculator.ui.theme.TextColor

/**
 * Element that creates a button used in the in-app keyboard
 *
 * @param icon icon displayed above [text]
 * @param text the text displayed on the button
 * @param onClick the action performed when the button is clicked
 */
@Composable
fun InputIconButton(icon: ImageVector, description: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.background(MainColor),
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = MainColor,
        )
    ) {
        Icon(
            icon,
            contentDescription = description,
            tint = TextColor,
        )
    }
}

/**
 * Element that creates an icon button used in the in-app keyboard
 *
 * @param text the text displayed on the button
 * @param onClick the action performed when the button is clicked
 * @param enabled whether the button is currently enabled
 */
@Composable
fun InputButton(text: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button(
        modifier = Modifier.background(MainColor),
        onClick = {
            if (enabled) {
                onClick()
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MainColor,
        )
    ) {
        Text(
            text = text,
            color = if (enabled) TextColor else TextColor.copy(alpha = 0.5f),
            style = TextStyle(fontWeight = FontWeight.Bold),
        )
    }
}