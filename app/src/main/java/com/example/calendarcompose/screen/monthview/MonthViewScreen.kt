package com.example.calendarcompose.screen.monthview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calendarcompose.screen.monthview.model.BorderOrder
import com.example.calendarcompose.screen.monthview.model.drawSegmentedBorder

@Composable
fun MonthViewScreen() {
    Column {
        Row(Modifier.padding(20.dp)) {
            repeat(4) {

                val order = when (it) {
                    0 -> BorderOrder.Start
                    1 -> BorderOrder.Center
                    2 -> BorderOrder.Center
                    3 -> BorderOrder.End
                    else -> BorderOrder.End
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .drawSegmentedBorder(
                            strokeWidth = 2.dp,
                            borderColor = Color.Green,
                            borderOrder = order,
                            cornerPercent = 50,
                            backgroundColor = Color.LightGray
                        )
                        .padding(4.dp)
                ) {
                    Text(text = "$it")
                }
            }
        }

        Row(Modifier.padding(20.dp)) {
            repeat(4) {

                val order = when (it) {
                    0 -> BorderOrder.Hole
                    1 -> BorderOrder.Start
                    2 -> BorderOrder.Center
                    3 -> BorderOrder.End
                    else -> BorderOrder.End
                }

                val mModifier = if (order == BorderOrder.Hole) {
                    Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.LightGray)
                        .border(
                            2.dp,
                            Color.Green,
                            shape = RoundedCornerShape(20.dp),
                        )
                } else {
                    Modifier.drawSegmentedBorder(
                        strokeWidth = 2.dp,
                        borderColor = Color.Green,
                        borderOrder = order,
                        cornerPercent = 50,
                        backgroundColor = Color.LightGray
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .then(mModifier)
                        .padding(4.dp)
                ) {
                    Text(text = "$it")
                }
            }
        }
    }
}


@Preview
@Composable
fun TestView() {
    MonthViewScreen()
}