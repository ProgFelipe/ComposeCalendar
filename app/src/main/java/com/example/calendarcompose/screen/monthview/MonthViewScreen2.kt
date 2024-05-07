package com.example.calendarcompose.screen.monthview

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.calendarcompose.screen.monthview.model.CalendarEntity
import com.example.calendarcompose.screen.monthview.model.Day
import com.example.calendarcompose.screen.monthview.model.Event
import kotlinx.coroutines.launch

private const val NUMBER_ROWS = 7

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthViewScreen2() {
    val list2 = CalendarEntity.getMockedCalendarEntity().days
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var eventsDetails by remember { mutableStateOf<List<Event>>(emptyList()) }
    val list = remember { mutableStateListOf<Day?>()}
    list.addAll(list2)

    Scaffold {
        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.clickable {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                },
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                // Sheet content
                Text(modifier = Modifier.fillMaxWidth(), text = "Events:")
                Spacer(modifier = Modifier.height(20.dp))

                eventsDetails.forEach {
                    Text(modifier = Modifier.fillMaxWidth().background(it.eventType.color), text = "ID:"+ it.id + " "+ it.name + "\n")
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }


        Column {
            Row(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth()
            ) {
                (getWeekDaysFromCurrentDay()).forEach { dayName ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .align(alignment = Alignment.CenterVertically)
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
                columns = GridCells.Fixed(NUMBER_ROWS)
            )
            {
                items(list.size) { index ->
                    if (list[index] == null) {
                        EmptyCard()
                    } else {
                        CalendarItem(list[index]!!, list, index) { events ->
                            eventsDetails = events
                            showBottomSheet = true
                        }
                    }
                }
            }
        }
    }
}