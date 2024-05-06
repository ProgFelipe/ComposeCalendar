package com.example.calendarcompose.screen.monthview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calendarcompose.screen.monthview.model.BorderOrder
import com.example.calendarcompose.screen.monthview.model.Day
import com.example.calendarcompose.screen.monthview.model.Event
import com.example.calendarcompose.screen.monthview.model.drawSegmentedBorder
import java.util.Calendar

private val COLUMN_HEIGHT = 100.dp
private val borderSize = 0.5.dp
private const val MAX_EVENTS_X_DAY = 3

@Composable
fun CalendarItem(day: Day, list: List<Day?>, index: Int, onClick: (events: List<Event>) -> Unit) {

    var componentWidth by remember { mutableFloatStateOf(0F) }
    val remainText = remember {
        mutableStateOf<String>("")
    }
    val density = LocalDensity.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(COLUMN_HEIGHT)
            .clickable { onClick(day.events) }
            //.background(Color.LightGray)
            //.border(borderSize, SolidColor(Color.Black), shape = RectangleShape)
            .drawBehind {
                drawRect(Color.Black)
            }
            .drawBehind {
                val w = this.size.width - 2f
                val h = this.size.height - 2f
                drawRect(Color.White, Offset(1f, 1f), size = Size(w, h))
            }
    ) {

        val cal: Calendar = Calendar.getInstance()
        cal.setTime(day.date)

        Text(text = "D:${cal.get(Calendar.DAY_OF_MONTH)}-E:${day.events.size}")
        var rows = 0
        day.events.forEachIndexed { eventIndex, event ->

            // calculate previous spaces
            val currentIndex = event.position - 1
            if (currentIndex >= 0) {
                var from = currentIndex
                while (from >= 0) {
                    val result = day.events.firstOrNull { it.position == from }
                    if (result == null) {
                        Text( text = "no $from")
                        rows++
                        //Spacer(modifier = Modifier.size(20.dp))
                    } else {
                        //Text(text = "si $from")
                        from = -1
                    }
                    from--
                }
            }
            if (rows == MAX_EVENTS_X_DAY) {
                Text(text = "More..")
                return
            }
            rows++

            val borderType = event.getBorderFromDate(day.date)
            val rowAlignment = when (borderType) {
                BorderOrder.Start -> Alignment.End
                BorderOrder.End -> Alignment.Start
                else -> Alignment.CenterHorizontally
            }
            Row(
                modifier = Modifier
                    .drawSegmentedBorder(
                        strokeWidth = 2.dp,
                        borderColor = Color.Black,
                        borderOrder = borderType,
                        cornerPercent = 10,
                        backgroundColor = event.eventType.color
                    )
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        componentWidth = coordinates.parentLayoutCoordinates?.size?.width?.toFloat() ?: 0F
                        //val dayOffset = event.getPositionInRange(day.date)
                        //text = getTextToDraw(event.name, borderType, componentWidth, dayOffset)
                    }
                    .align(rowAlignment)
            ) {
                val modifier = Modifier
                var textAlign : TextAlign = TextAlign.End
                if (borderType == BorderOrder.Hole || borderType == BorderOrder.Center) {
                    modifier.fillMaxWidth()
                    modifier.align(Alignment.CenterVertically)
                    textAlign = TextAlign.Center
                }
                if (borderType == BorderOrder.End) {
                    textAlign = TextAlign.Start
                    // modifier.padding(horizontal = 10.dp)
                }
                if (borderType == BorderOrder.Hole) {
                    textAlign = TextAlign.Center
                }
                val textAlignment = when (borderType) {
                    BorderOrder.Start -> TextAlign.Start
                    BorderOrder.End -> TextAlign.Start
                    else -> TextAlign.Justify
                }

                val textToDraw = if (borderType == BorderOrder.Center || borderType == BorderOrder.End) {
                    list[index - 1]?.events?.find { eventx -> eventx.id == event.id }?.remainText ?: ""
                } else {
                    event.name
                }

                /*if (borderType == BorderOrder.End) {
                    Text(
                        modifier = modifier.fillMaxWidth(), text = textToDraw, maxLines = 1,
                        // textAlign = textAlignment,
                        textAlign = TextAlign.Center,
                        onTextLayout = { textLayoutResult ->
                            if (textLayoutResult.hasVisualOverflow) {
                                val lineEndIndex = textLayoutResult.getLineEnd(
                                    lineIndex = 0,
                                    visibleEnd = false
                                )
                                event.remainText = textToDraw.substring(lineEndIndex)
                            }
                        }
                    )
                } else {*/
                Text(
                    modifier = modifier.fillMaxWidth(), text = textToDraw, maxLines = 1,
                    // overflow = TextOverflow.Ellipsis,
                    textAlign = textAlign,
                    onTextLayout = { textLayoutResult ->
                        if (textLayoutResult.hasVisualOverflow) {
                            val lineEndIndex = textLayoutResult.getLineEnd(
                                lineIndex = 0,
                                visibleEnd = true
                            )
                            event.remainText = textToDraw.substring(lineEndIndex)
                        }
                    }
                )

            }
            //Text(text = borderType.name)
            //Text(text = borderType.name)
            //Text(text = "${event.getDayOfMonth(event.starDate)} - ${event.getDayOfMonth(event.endDate)}")
        }
    }
}


@Composable
fun EmptyCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(COLUMN_HEIGHT)
        //.border(borderSize, SolidColor(Color.Black), shape = RectangleShape)
    ) {
    }
}


var explited = false
fun getTextToDraw(text: String, borderType: BorderOrder, boxWidth: Float, position: Int): String {
    // get text width

    val characterPerWidth = boxWidth / 1.5
    //val splitted = text.chunked(8)
    val splitted = text.split(" ")

    return if (splitted.size - 1 < position) {
        ""
    } else {
        splitted[position]
    }
    /*return when(borderType){
        BorderOrder.Start -> splitted[0]
        BorderOrder.Center -> if(splitted.size > 1) { splitted[1] } else{ ""}
        BorderOrder.End ->   if(splitted.size > 1) { splitted[1] } else{ ""}
        BorderOrder.Hole ->   if(splitted.size > 1) { splitted.last() } else{ ""}
    }*/

}