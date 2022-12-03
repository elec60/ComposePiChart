package com.example.composepichart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composepichart.ui.theme.ComposePiChartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePiChartTheme {
                Box(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    PiChart(
                        radius = 150.dp,
                        innerCircleRadius = 60.dp,
                        items = listOf(
                            MyData(
                                text = "Kotlin",
                                color = Color.Magenta,
                                amount = 100
                            ),
                            MyData(
                                text = "Swift",
                                color = Color.Yellow,
                                amount = 70
                            ),
                            MyData(
                                text = "C#",
                                color = Color.Green,
                                amount = 80
                            ),
                            MyData(
                                text = "Python",
                                color = Color.Blue,
                                amount = 40
                            ),
                            MyData(
                                text = "Ruby",
                                color = Color.Red,
                                amount = 33
                            ),
                            MyData(
                                text = "Go",
                                color = Color.Cyan,
                                amount = 75
                            )
                        )
                    )
                }
            }
        }
    }
}
