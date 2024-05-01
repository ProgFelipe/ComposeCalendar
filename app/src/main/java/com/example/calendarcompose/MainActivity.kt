package com.example.calendarcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import com.example.calendarcompose.model.DayEntity
import com.example.calendarcompose.model.EventEntity
import com.example.calendarcompose.screen.monthview.LocationLabelArea
import com.example.calendarcompose.screen.monthview.MonthViewScreen
import com.example.calendarcompose.screen.monthview.MonthViewScreen2
import com.example.calendarcompose.screen.threeday.ThreeDayScreen
import java.util.Date
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val items = listStateProvidedByViewModel()
        val fixedItems = listStateProvidedByViewModel(true)

        setContent {
            // ThreeDayScreen(items, fixedItems)
            MonthViewScreen2()
            /*LocationLabelArea(
                locations = "Some location, Some, Some location, Some location, Some location"
            )*/
        }
    }
}


// Elements sent and updated in viewModel
// example of list [ [day1, listOfEvents{}, day, listOfEvents{}]]
// use mutableStateListOf<ListItem> instead of fixed list
fun listStateProvidedByViewModel(fixed: Boolean = false): List<DayEntity> {
    return (1..100).map {
        DayEntity(
            name = "Day $it",
            numberOfDays = Random.nextInt(1, 5),
            color = Color(
                Random.nextLong(0xFFFFFFFF)
            ).copy(alpha = 1f),
            events = getEvents(fixed),
        )
    }
}

private fun getEvents(fixed: Boolean): List<EventEntity> {
    val eventsSize = if (fixed) 5 else Random.nextInt(1, 5)
    return (0..eventsSize).map { index ->
        EventEntity("Text $index", Date(), Date())
    }
}
