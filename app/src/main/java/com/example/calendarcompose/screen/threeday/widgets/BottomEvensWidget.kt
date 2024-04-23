package com.example.calendarcompose.screen.threeday.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendarcompose.model.DayEntity
import com.example.calendarcompose.model.EventEntity
import kotlin.random.Random


@Composable
fun BottomEvensWidget(width: Dp) {
    val list = (1..100).map { it.toString() }

    LazyVerticalGrid(
        modifier = Modifier.width(width),
        columns = GridCells.Fixed(3),
        // content padding
        /*contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),*/
        content = {
            items(list.size) { index ->
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Red, //Card background color
                        contentColor = Color.White  //Card content color,e.g.text
                    ),
                    elevation = CardDefaults.cardElevation(),
                ) {
                    Text(
                        text = list[index],
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    )
}

// Other way to show this bottom widget
fun LazyListScope.secondContainer(dayFixed: List<DayEntity>, columnWidth: Dp, stateRowZ: LazyListState) {
    items(dayFixed) { day ->
        LazyColumn(
            modifier = Modifier.width(columnWidth),
            state = stateRowZ
        ) {
            items(day.events) { event ->
                EventWidget2(event, columnWidth)
            }
        }
    }
}

@Composable
fun EventWidget2(item: EventEntity, columnWidth: Dp) {
    Box(
        modifier = Modifier
            .height(200.dp)
            .background(
                Color(
                    Random.nextLong(0xFFFFFFFF)
                )
            )
            .padding(bottom = 40.dp)
    ) {
        Text(
            text = item.text,
            modifier = Modifier
                .height(200.dp)
                .width(columnWidth)
                .wrapContentHeight(align = Alignment.CenterVertically),
            textAlign = TextAlign.Center,
        )
    }
}
