package com.example.calendarcompose.screen.monthview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calendarcompose.screen.monthview.model.BorderOrder
import com.example.calendarcompose.screen.monthview.model.CalendarEntity
import com.example.calendarcompose.screen.monthview.model.Day
import com.example.calendarcompose.screen.monthview.model.drawSegmentedBorder
import java.util.Calendar

private const val NUMBER_ROWS = 7

@Composable
fun MonthViewScreen() {
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
                    .size(80.dp)
                    .drawSegmentedBorder(
                        strokeWidth = 2.dp,
                        color = Color.Green,
                        borderOrder = order,
                        cornerPercent = 40,
                        drawDivider = false
                    )
                    .padding(4.dp)
            ) {
                Text(text = "$it")
            }
        }
    }


    /*Row {
        repeat(4) {

            val order = when (it) {
                0 -> BorderOrder.Start
                3 -> BorderOrder.End
                else -> BorderOrder.Center
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .drawSegmentedBorder(
                        strokeWidth = 2.dp,
                        color = Color.Cyan,
                        borderOrder = order,
                        cornerPercent = 50,
                        drawDivider = true
                    )
                    .padding(4.dp)
            ) {
                Text(text = "$it")
            }
        }
    }*/
}

private val COLUMN_HEIGHT = 100.dp

@Composable
fun MonthViewScreen2() {
    val list = CalendarEntity.calendarTest().days
    val daysTitle = listOf(
        Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY
    )
    val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    val dayName = when (currentDayOfWeek) {
        Calendar.SUNDAY -> "Sun"
        Calendar.MONDAY -> "Mon"
        Calendar.TUESDAY -> "Tue"
        Calendar.WEDNESDAY -> "Wen"
        Calendar.THURSDAY -> "Thu"
        Calendar.FRIDAY -> "Fri"
        Calendar.SATURDAY -> "Sat"
        else -> {
            ""
        }
    }
    Column {
        Row(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth()
        ) {
            // Text(modifier = Modifier.background(Color.Cyan), text = dayName)
            (1..7).forEach {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center), text = dayName, style = TextStyle(
                            fontWeight =
                            FontWeight.Bold
                        )
                    )
                }
            }
        }

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize(),
            columns = GridCells.Fixed(NUMBER_ROWS),
            // content padding
            /*contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),*/
            content = {
                items(list.size) { index ->
                    if (list[index] == null) {
                        EmptyCard()
                    } else {
                        CalendarItem(list[index]!!)
                    }
                }
            }
        )
    }
}

@Composable
fun EmptyCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(COLUMN_HEIGHT)
            .border(1.dp, SolidColor(Color.DarkGray), shape = RectangleShape)
    ) {
    }
}

@Composable
fun CalendarItem(day: Day) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(COLUMN_HEIGHT)
            .background(Color.LightGray)
            .border(1.dp, SolidColor(Color.DarkGray), shape = RectangleShape)
    ) {

        val cal: Calendar = Calendar.getInstance()
        cal.setTime(day.date)

        Text(text = cal.get(Calendar.DAY_OF_MONTH).toString())
        day.events.forEach {
            val borderType = it.getBorderFromDate(day.date)
            val rowAlignment = when (borderType) {
                BorderOrder.Start -> Alignment.End
                BorderOrder.End -> Alignment.Start
                else -> Alignment.CenterHorizontally
            }
            Row(
                modifier = Modifier
                    .background(
                        color = it.eventType.color,
                        shape = RoundedCornerShape(2.dp)
                    )
                    .drawSegmentedBorder(
                        strokeWidth = 2.dp,
                        color = Color.Green,
                        borderOrder = borderType,
                        cornerPercent = 40,
                        drawDivider = false
                    )
                    .align(rowAlignment)
            ) {
                val modifier = Modifier
                if (borderType == BorderOrder.HOLE || borderType == BorderOrder.Center) {
                    modifier.fillMaxWidth()
                }
                val textAlignment = when (borderType) {
                    BorderOrder.Start -> TextAlign.End
                    BorderOrder.End -> TextAlign.Start
                    else -> TextAlign.Justify
                }
                Text(modifier = modifier, text = it.name, textAlign = textAlignment)
            }
        }
    }
}

@Preview
@Composable
fun TestView() {
    MonthViewScreen()
}