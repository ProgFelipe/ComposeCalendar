package com.example.calendarcompose.screen.monthview

import java.util.Calendar

fun getWeekDaysFromCurrentDay(): List<String> {
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

    val currentDay = daysTitle.indexOfFirst { it.first == currentDayOfWeek }
    var counter = currentDay

    val daysFromCurrentDay = arrayListOf<String>()
    daysTitle.map { _ ->
        daysFromCurrentDay.add(daysTitle[counter].second)
        if (counter == daysTitle.size - 1) {
            counter = 0
        } else {
            counter++
        }
    }
    return daysFromCurrentDay
}