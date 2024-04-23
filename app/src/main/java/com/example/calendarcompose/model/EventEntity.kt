package com.example.calendarcompose.model

import java.util.Date

data class EventEntity(
    val text: String,
    val startDay: Date,
    val endDay: Date
)
