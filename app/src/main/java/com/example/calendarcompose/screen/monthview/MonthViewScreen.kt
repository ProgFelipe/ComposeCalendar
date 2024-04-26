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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
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

                val mModifier = if(order == BorderOrder.Hole){
                    Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.LightGray)
                        .border(
                            2.dp,
                            Color.Green,
                            shape = RoundedCornerShape(20.dp),
                        )
                }else{
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

private val COLUMN_HEIGHT = 100.dp

fun getWeekDaysFromCurrentDay() : List<String> {
    val daysTitle = listOf(
        Pair(Calendar.SUNDAY, "Sun"),
        Pair(Calendar.MONDAY, "Mon"),
        Pair(Calendar.TUESDAY, "Tue"),
        Pair(Calendar.WEDNESDAY, "Wen"),
        Pair(Calendar.THURSDAY, "Thu"),
        Pair(Calendar.FRIDAY, "Fri"),
        Pair(Calendar.SATURDAY, "Sat")
    )
    val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

    val currentDay = daysTitle.indexOfFirst { it.first ==  currentDayOfWeek}
    var counter = currentDay

    val daysFromCurrentDay = arrayListOf<String>()
    daysTitle.map { _ ->
        daysFromCurrentDay.add(daysTitle[counter].second)
        if(counter == daysTitle.size - 1){
            counter = 0
        } else {
            counter++
        }
    }
    return daysFromCurrentDay
}

@Composable
fun MonthViewScreen2() {
    val list = CalendarEntity.calendarTest().days

    Column {
        Row(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth()
        ) {
            // Text(modifier = Modifier.background(Color.Cyan), text = dayName)
            (getWeekDaysFromCurrentDay()).forEach { dayName ->
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

fun Modifier.drawWithoutRect(rect: Rect?) =
    drawWithContent {
        if (rect != null) {
            clipRect(
                left = rect.left,
                top = rect.top,
                right = rect.right,
                bottom = rect.bottom,
                clipOp = ClipOp.Difference,
            ) {
                this@drawWithContent.drawContent()
            }
        } else {
            drawContent()
        }
    }

@Composable
fun CalendarItem(day: Day) {
    var textCoordinates by remember { mutableStateOf <Rect?>(null) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(COLUMN_HEIGHT)
            .background(Color.LightGray)
            .drawWithoutRect(textCoordinates)
            .border(
                width = 0.8.dp,
                color = Color.Black.copy(alpha = 0.5f),
                shape = RectangleShape
            )
            //.border(1.dp, SolidColor(Color.DarkGray), shape = RectangleShape)
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
                    .onGloballyPositioned { layoutCoordinates ->
                        textCoordinates = layoutCoordinates.boundsInParent()
                    }
                    .drawSegmentedBorder(
                        strokeWidth = 2.dp,
                        borderColor = Color.Green,
                        borderOrder = borderType,
                        cornerPercent = 20,
                        backgroundColor = Color.White
                    )
                    .fillMaxWidth()
                    .align(rowAlignment)
            ) {
                val modifier = Modifier
                if (borderType == BorderOrder.Hole || borderType == BorderOrder.Center) {
                    modifier.fillMaxWidth()
                }
                val textAlignment = when (borderType) {
                    BorderOrder.Start -> TextAlign.End
                    BorderOrder.End -> TextAlign.Start
                    else -> TextAlign.Justify
                }
                Text(modifier = modifier, text = it.name, textAlign = textAlignment)
            }
            Text(text = borderType.name)
            Text(text = "${it.getDayOfMonth(it.starDate)} - ${it.getDayOfMonth(it.endDate)}")
        }
    }
}

@Preview
@Composable
fun TestView() {
    MonthViewScreen()
}