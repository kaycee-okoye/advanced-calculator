package com.oaktech.advancedcalculator.pages

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oaktech.advancedcalculator.composable_functions.SingleButtonAppbar
import com.oaktech.advancedcalculator.ui.theme.AccentColor
import com.oaktech.advancedcalculator.ui.theme.AdvancedCalculatorTheme
import com.oaktech.advancedcalculator.ui.theme.MainColor
import com.oaktech.advancedcalculator.ui.theme.ShadowColor
import com.oaktech.advancedcalculator.ui.theme.TextColor

class ModeMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenUI()
        }
    }

    /**
     * Element that displays the mode menu options
     */
    @Composable
    private fun ScreenUI() {
        val modes =
            listOf<String>(
                "Scientific Calculator",
                "Polynomial Roots",
                "Linear Equations",
                "Table Generator",
                "Graphing Calculator"
            )
        val destinations: List<Intent> = listOf<Intent>(
            Intent(this@ModeMenu, ScientificCalculator::class.java),
            Intent(this@ModeMenu, PolynomialRootFinder::class.java),
            Intent(this@ModeMenu, LinearSystemOfEquationsSolver::class.java),
            Intent(this@ModeMenu, TableGenerator::class.java),
            Intent(this@ModeMenu, GraphingCalculator::class.java),
        )
        AdvancedCalculatorTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MainColor
            ) {
                Column(modifier = Modifier.fillMaxHeight()) {
                    SingleButtonAppbar(
                        icon = Icons.Default.ArrowBack,
                        description = "Back",
                        onClick = {
                            finish()
                        },
                        Alignment.CenterStart
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 150.dp)
                    ) {
                        items(modes) { mode ->
                            ModeCard(title = mode, destination = destinations[modes.indexOf(mode)])
                        }
                    }
                }
            }
        }
    }

    /**
     * Element that displays a menu option
     *
     * @param title the menu option's title
     * @param destination the page to navigate to if the menu option
     * is clicked
     */
    @Composable
    private fun ModeCard(title: String, destination: Intent) {
        Box(
            modifier = Modifier.padding(10.dp).fillMaxWidth()
                .shadow(
                    ambientColor = ShadowColor,
                    elevation = 5.dp,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(MainColor)
                .aspectRatio(0.9f)
                .clickable {
                    startActivity(destination)
                }
        ) {
            Column(
                modifier = Modifier.padding(10.dp).fillMaxWidth().fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .width(50.dp)
                        .aspectRatio(1f)
                        .background(AccentColor, shape = CircleShape)
                        .weight(2f),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                    color = TextColor
                )
            }
        }
    }

    /**
     * Preview of the menu options page
     */
    @Preview(showBackground = true)
    @Composable
    private fun PagePreview() {
        ScreenUI()
    }
}