package com.example.composepichart

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt

@Composable
fun PiChart(
    radius: Dp,
    innerCircleRadius: Dp,
    items: List<MyData>
) {

    val sumOfAmounts = remember(items) {
        items.sumOf { it.amount }
    }

    var selectedIndex by remember {
        mutableStateOf(-1)
    }

    var itemsState by remember(items) {
        mutableStateOf(items)
    }

    var center by remember {
        mutableStateOf(Offset.Zero)
    }

    val scaleAnims = mutableListOf<State<Float>>()

    val sweepAngleAnimatableList = mutableListOf<Animatable<Float, AnimationVector1D>>()
    repeat(itemsState.size) { index ->
        scaleAnims.add(
            animateFloatAsState(
                targetValue = if (selectedIndex == index) 0f else 1f,
                animationSpec = tween(durationMillis = 400, easing = EaseOutBounce)
            )
        )
        sweepAngleAnimatableList.add(
            remember {
                Animatable(initialValue = 0f)
            }
        )
    }

    sweepAngleAnimatableList.forEach { animatable ->
        LaunchedEffect(key1 = animatable) {
            animatable.animateTo(
                1f,
                animationSpec = tween(
                    durationMillis = 1500,
                    easing = EaseOutBounce
                )
            )
        }
    }

    Canvas(
        modifier = Modifier
            .size(radius * 2)
            .pointerInput(true) {
                detectTapGestures(
                    onTap = { offset ->
                        val r =
                            sqrt((offset.x - center.x) * (offset.x - center.x) + (offset.y - center.y) * (offset.y - center.y))
                        val innerCircleClicked = r <= innerCircleRadius.toPx()
                        if (innerCircleClicked) return@detectTapGestures

                        val tapAngle = ((-atan2(
                            x = center.y - offset.y,
                            y = center.x - offset.x
                        ) * 180 / PI).toFloat() + 360f - 90f) % 360f

                        selectedIndex = -1
                        var startAngle = 0f
                        for (i in itemsState.indices) {
                            val item = itemsState[i]
                            val sweepAngle = item.amount.toFloat() / sumOfAmounts * 360f
                            if (tapAngle >= startAngle && tapAngle <= startAngle + sweepAngle) {
                                selectedIndex = i
                                break
                            }
                            startAngle += sweepAngle
                        }

                        if (selectedIndex != -1) {
                            itemsState = itemsState.mapIndexed { index, item ->
                                item.copy(isSelected = index == selectedIndex)
                            }
                        }
                    }
                )
            }

    ) {
        val width = this.size.width
        val height = this.size.height

        center = Offset(x = width / 2, y = height / 2)

        var startAngle = 0f
        itemsState.forEachIndexed { index, item ->
            val sweepAngle = item.amount.toFloat() / sumOfAmounts * 360f
            val animValue = scaleAnims[index].value
            scale(1 - (animValue - 1) / 5f) {
                drawArc(
                    color = item.color,
                    startAngle = startAngle - 5 * (animValue - 1),
                    sweepAngle = sweepAngleAnimatableList[index].value * sweepAngle,
                    useCenter = true,
                    size = Size(
                        width = radius.toPx() * 2,
                        height = radius.toPx() * 2
                    )
                )
            }
            startAngle += sweepAngle
        }

        drawCircle(
            color = Color.White.copy(alpha = 0.5f),
            radius = innerCircleRadius.toPx() * 1.2f
        )

        drawCircle(
            color = Color.White,
            radius = innerCircleRadius.toPx()
        )

    }

}

data class MyData(
    val text: String,
    val color: Color,
    val amount: Int,
    val isSelected: Boolean = false
)