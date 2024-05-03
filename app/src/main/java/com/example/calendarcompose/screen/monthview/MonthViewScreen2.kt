package com.example.calendarcompose.screen.monthview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.calendarcompose.screen.monthview.model.CalendarEntity

private const val NUMBER_ROWS = 7

@Composable
fun MonthViewScreen2() {
    val list = CalendarEntity.getMockedCalendarEntity().days

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
                        CalendarItem(list[index]!!, list, index)
                    }
                }
            }
        )
    }
}