package com.example.calendarcompose.screen.monthview.model

import androidx.compose.ui.graphics.Color
import androidx.core.util.toRange
import java.time.Duration
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.random.Random

data class CalendarEntity(
    val days: List<Day?> = listOf()
) {
    companion object {
        fun calendarTest(): CalendarEntity {
            val person = Person("Artur", Color.Blue)
            val person2 = Person("Charles", Color.Green)
            val person3 = Person("Violet", Color.Cyan)
            val calendar: Calendar = Calendar.getInstance()
            val daySample = Day(
                events = listOf(
                    Event(
                        1,
                        "Maria's Birthday is today awesome",
                        EventType.BIRTHDAY,
                        persons = listOf(person, person2, person3),
                        calendar.time,
                        calendar.also { it.add(Calendar.DATE, 2) }
                            .time))
            )
            val daySample2 = Day(
                events = listOf(
                    /*Event("one", EventType.APPOINTMENT, persons = listOf(person, person3), calendar.time, calendar.time),
                    Event("two", EventType.BIRTHDAY, persons = listOf(person, person3), calendar.time, calendar.also { it.add(Calendar.DATE, 1) }
                        .time),*/
                    Event(2, "three", EventType.STUDY, persons = listOf(person, person3), calendar.time, calendar.also { it.add(Calendar.DATE, 2) }
                        .time)
                )
            )
            val daySample3 = Day(
                events = listOf(Event(3,"five", EventType.APPOINTMENT, persons = listOf(person2, person3), calendar.time,
                    calendar.also { it.add(Calendar.DATE, 5) }.time))
            )
            val days = listOf(daySample, daySample2, daySample3)
            val list = arrayListOf<Day?>()
            var extraSpaces = 0

            val dateInfo = GregorianCalendar()

            (0..200).forEach { index ->
                // Add empty view
                if ((list.size - extraSpaces) % 30 == 0) {
                    if (list.size > 0) {
                        list.add(null)
                        list.add(null)
                        list.add(null)
                        extraSpaces += 3
                    }
                    list.add(null)
                    list.add(null)
                    extraSpaces += 2
                }
                // val day = days[Random.nextInt(0, 2)]
                val day = if(index < 3) {daySample}else{daySample2}

                val change = day.copy()
                change.date = dateInfo.time
                list.add(change)
                dateInfo.roll(Calendar.DAY_OF_YEAR, true)
            }
            return CalendarEntity(
                list
            )
        }
    }
}

data class Person(
    val name: String = "Test",
    val iconColor: Color
)

enum class EventType(val color: Color) {
    BIRTHDAY(Color.LightGray),
    APPOINTMENT(Color.Blue),
    STUDY(Color.Green)
}

data class Event(
    val id : Int,
    val name: String = "",
    val eventType: EventType,
    val persons: List<Person> = listOf(),
    val starDate: Date,
    val endDate: Date,
    var remainText: String = ""
) {
    fun getBorderFromDate(date: Date) : BorderOrder {
        if(starDate == endDate){
            return BorderOrder.Hole
        }
        return when(date){
            starDate -> BorderOrder.Start
            endDate -> BorderOrder.End
            else -> BorderOrder.Center
        }
    }

    fun getDayOfMonth(date: Date): String {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.DAY_OF_MONTH).toString()
    }

    fun getPositionInRange(date: Date) : Int {
        //TimeUnit.MILLISECONDS.toDays(endDate.day - date.day)


        /*val cal = Calendar.getInstance()
        cal.time = date
        val day1 = cal.get(Calendar.DAY_OF_MONTH)

        val cal2 = Calendar.getInstance()
        cal2.time = starDate
        val day2 = cal2.get(Calendar.DAY_OF_MONTH)*/

        val days = TimeUnit.DAYS.convert(starDate.time-date.time, TimeUnit.MILLISECONDS)


        //val daysRounded = (endDate.time - date.time / (1000.0 * 60 * 60 * 24)).roundToInt()
        return  days.toInt().absoluteValue
    }
}

data class Day(
    var date: Date = Date(),
    val events: List<Event> = emptyList()
)
