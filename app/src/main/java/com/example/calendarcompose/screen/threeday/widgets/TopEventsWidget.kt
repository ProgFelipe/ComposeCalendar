package com.example.calendarcompose.screen.threeday.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.calendarcompose.model.DayEntity
import com.example.calendarcompose.model.EventEntity
import kotlin.random.Random

@Composable
fun TopEventsWidget(day: DayEntity, width: Dp) {
    LazyColumn(
        modifier = Modifier.width(width),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            HeaderWidget(day.name)
        }
        items(day.events.size) {
            EventWidget(day.events[it])
        }
    }
}

@Composable
fun HeaderWidget(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp),
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
fun EventWidget(item: EventEntity) {
    Text(
        text = item.text,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                Color(
                    Random.nextLong(0xFFFFFFFF)
                )
            ),
        textAlign = TextAlign.Center,
    )
}