package com.oaktech.advancedcalculator.composable_functions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oaktech.advancedcalculator.ui.theme.AccentColor
import com.oaktech.advancedcalculator.ui.theme.MainColor
import com.oaktech.advancedcalculator.ui.theme.TextColor

/**
 * Element that creates a floating app-bar that contains an order management display
 *
 * @param currentOrder the current order of the equations being solved
 */
@Composable
fun AppbarWithEditableOrder(
    currentOrder: Int,
    onBack: () -> Unit,
    onReduce: () -> Unit,
    onIncrease: () -> Unit
) {
    Appbar {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = AccentColor),
                onClick = onBack,
                modifier = Modifier.padding(5.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MainColor,
                    modifier = Modifier.size(48.dp).padding(10.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(
                    onClick = onReduce,
                    modifier = Modifier.padding(5.dp)
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Reduce Order",
                        tint = TextColor,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Text(
                    text = "n = ${currentOrder}",
                    fontSize = 30.sp,
                    color = TextColor,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                IconButton(
                    onClick = onIncrease,
                    modifier = Modifier.padding(5.dp)
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = "Increase Order",
                        tint = TextColor,
                        modifier = Modifier.size(50.dp)
                    )
                }

            }
            Spacer(Modifier.width(58.dp))
        }
    }
}

/**
 * Element that creates a floating app-bar
 *
 * @param content the content of the app-bar
 */
@Composable
fun Appbar(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.background(color = MainColor)
            .fillMaxWidth()
    ) {
        content()
    }
}

/**
 * Element that creates a floating app-bar with a single icon button
 *
 * @param icon the icon displayed on the button
 * @param description the button's content description
 * @param onClick the action performed when the button is clicked
 * @param alignment where to place the icon button in the transparent app-bar
 */
@Composable
fun SingleButtonAppbar(
    icon: ImageVector,
    description: String,
    onClick: () -> Unit,
    alignment: Alignment
) {
    Box(
        contentAlignment = alignment,
        modifier = Modifier.background(color = MainColor)
            .fillMaxWidth()
    ) {
        IconButton(
            colors = IconButtonDefaults.filledIconButtonColors(containerColor = AccentColor),
            onClick = onClick,
            modifier = Modifier.padding(5.dp)
        ) {
            Icon(
                icon,
                contentDescription = description,
                tint = MainColor,
                modifier = Modifier.size(48.dp).padding(10.dp)
            )
        }
    }
}