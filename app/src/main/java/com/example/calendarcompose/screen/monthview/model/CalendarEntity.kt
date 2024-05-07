package com.example.calendarcompose.screen.monthview.model

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.UUID
import kotlin.random.Random
import kotlin.random.asKotlinRandom

data class CalendarEntity(
    val days: List<Day?> = listOf()
) {

    companion object {
        private const val NUMBER_OF_ELEMENTS = 2000

        private const val MAX_EVENTS_X_DAY = 3
        private const val MAX_DAYS_X_EVENT = 5
        private const val EMPTY_SPACES = 7

        fun getMockedCalendarEntity(): CalendarEntity {

            val list = arrayListOf<Day?>()
            val dateInfo = GregorianCalendar()
            val positionsPerDay = (0..MAX_EVENTS_X_DAY)

            var addExtraSpaces = false

            (0..NUMBER_OF_ELEMENTS).forEach { _ ->

                val daysInMonth = dateInfo.getActualMaximum(Calendar.DAY_OF_MONTH)
                val day = dateInfo.get(Calendar.DAY_OF_MONTH)

                // region addSpaces
                if (addExtraSpaces) {
                    (1..EMPTY_SPACES).forEach { _ ->
                        list.add(null)
                    }
                }
                // endregion

                val daysOffset = if (addExtraSpaces) {
                    addExtraSpaces = false
                    EMPTY_SPACES + 1
                } else {
                    1
                }
                if (day == daysInMonth) {
                    addExtraSpaces = true
                }

                val listOfEventsXDay = arrayListOf<Event>()
                if (list.isNotEmpty()) {
                    val previousDayEvents = list[list.size - daysOffset]?.events
                    addPreviousEventsPresentOnThisDay(previousDayEvents, dateInfo, listOfEventsXDay)
                }

                val availablePositions = getRemainPositions(listOfEventsXDay, positionsPerDay)

                if (listOfEventsXDay.size < MAX_EVENTS_X_DAY) {
                    for (element in availablePositions) {
                        listOfEventsXDay.add(generateEvent(element, dateInfo.time))
                    }
                }

                listOfEventsXDay.sortBy { it.position }

                list.add(Day(dateInfo.time, events = listOfEventsXDay))
                dateInfo.add(Calendar.DATE, 1)
            }

            return CalendarEntity(list)
        }

        private fun addPreviousEventsPresentOnThisDay(
            list: List<Event>?,
            currentDay: GregorianCalendar,
            listOfEventsXDay: ArrayList<Event>
        ) {
            list?.map {
                Log.d("CALC", "${it.endDate} >= ${currentDay.time}")
                if (it.endDate >= currentDay.time) {
                    Log.d("CALC", "ADDED")
                    listOfEventsXDay.add(it)
                }
            }
        }

        private fun getRemainPositions(
            listOfEventsXDay: ArrayList<Event>,
            positionsPerDay: IntRange
        ): List<Int> {
            val eventsPositionInColumn = listOfEventsXDay.map { it.position }
            return positionsPerDay.filter { it !in eventsPositionInColumn }.sorted()
        }

        private fun generateEvent(position: Int, initDay: Date): Event {
            val person = Person("Artur", Color.Blue)
            val person2 = Person("Charles", Color.LightGray)
            val person3 = Person("Violet", Color.Cyan)

            val calendar: Calendar = Calendar.getInstance()
            calendar.time = initDay

            val initDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val endCalendar = calendar.also { it.add(Calendar.DAY_OF_YEAR, Random.nextInt(0, MAX_DAYS_X_EVENT)) }

            val endDayOfMonth = endCalendar.get(Calendar.DAY_OF_MONTH)
            val endDay = endCalendar.time

            val random = java.util.Random().asKotlinRandom()

            return Event(
                UUID.randomUUID().toString(),
                //"$position $initDayOfMonth - $endDayOfMonth",
                LoremIpsum(20).values.joinToString(),
                EventType.entries.toTypedArray().random(random),
                persons = listOf(person, person2, person3),
                initDay,
                endDay,
                position = position
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
    APPOINTMENT(Color.Magenta),
    STUDY(Color.Green)
}

data class Event(
    val id: String,
    val name: String = "",
    val eventType: EventType,
    val persons: List<Person> = listOf(),
    val starDate: Date,
    val endDate: Date,
    // utils to draw
    var remainText: String = "",
    var position: Int = 0
) {
    fun getBorderFromDate(date: Date): BorderOrder {
        if (starDate == endDate) {
            return BorderOrder.Hole
        }
        return when (date) {
            starDate -> BorderOrder.Start
            endDate -> BorderOrder.End
            else -> BorderOrder.Center
        }
    }
}

data class Day(
    var date: Date = Date(),
    val events: List<Event> = emptyList()
)
