package com.example.calendarcompose.screen.monthview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calendarcompose.screen.monthview.model.BorderOrder
import com.example.calendarcompose.screen.monthview.model.Day
import com.example.calendarcompose.screen.monthview.model.drawSegmentedBorder
import java.util.Calendar

private val COLUMN_HEIGHT = 100.dp
private val borderSize = 0.5.dp

@Composable
fun CalendarItem(day: Day) {

    var componentWidth by remember { mutableFloatStateOf(0F) }
    var text by remember { mutableStateOf("") }
    val density = LocalDensity.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(COLUMN_HEIGHT)
            .background(Color.LightGray)
            .drawBehind {
                drawRect(Color.Black)
            }
            .drawBehind {
                drawRect(Color.White, Offset(1f, 1f))
            }
    ) {


        val cal: Calendar = Calendar.getInstance()
        cal.setTime(day.date)

        Text(text = cal.get(Calendar.DAY_OF_MONTH).toString())
        day.events.forEach { it ->
            val borderType = it.getBorderFromDate(day.date)
            val rowAlignment = when (borderType) {
                BorderOrder.Start -> Alignment.End
                BorderOrder.End -> Alignment.Start
                else -> Alignment.CenterHorizontally
            }
            Row(
                modifier = Modifier
                    .drawSegmentedBorder(
                        strokeWidth = 2.dp,
                        borderColor = Color.Green,
                        borderOrder = borderType,
                        cornerPercent = 10,
                        backgroundColor = it.eventType.color
                    )
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        componentWidth = coordinates.parentLayoutCoordinates?.size?.width?.toFloat() ?: 0F
                        val dayOffset = it.getPositionInRange(day.date)
                        text = getTextToDraw(it.name, borderType, componentWidth, dayOffset)
                    }
                    .align(rowAlignment)
            ) {
                val modifier = Modifier
                if (borderType == BorderOrder.Hole || borderType == BorderOrder.Center) {
                    modifier.fillMaxWidth()
                }
                if (borderType == BorderOrder.Hole) {
                    // modifier.padding(horizontal = 10.dp)
                }
                val textAlignment = when (borderType) {
                    BorderOrder.Start -> TextAlign.End
                    BorderOrder.End -> TextAlign.Start
                    else -> TextAlign.Justify
                }


                    Text(
                        modifier = modifier.width(componentWidth.dp), text = text, maxLines = 1,
                        //overflow = TextOverflow.Ellipsis,
                        textAlign = textAlignment
                    )

            }
            Text(text = borderType.name)
            Text(text = "${it.getDayOfMonth(it.starDate)} - ${it.getDayOfMonth(it.endDate)}")
        }
    }
}


@Composable
fun EmptyCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(COLUMN_HEIGHT)
            .border(borderSize, SolidColor(Color.Black), shape = RectangleShape)
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