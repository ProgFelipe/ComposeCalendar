package com.example.calendarcompose.model

import androidx.compose.ui.graphics.Color

data class DayEntity(
    val name: String,
    val numberOfDays: Int,
    val color: Color,
    val events: List<EventEntity>
)