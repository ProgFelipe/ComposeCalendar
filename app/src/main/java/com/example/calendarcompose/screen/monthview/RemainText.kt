package com.example.calendarcompose.screen.monthview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LocationLabelArea(locations: String) {
    val remainText = remember {
        mutableStateOf<String>("")
    }
    Column {
        Row(
            modifier = Modifier
                .height(48.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var additional by remember { mutableStateOf(10) }

            Box(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = locations,
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    onTextLayout = { textLayoutResult ->
                        if (textLayoutResult.hasVisualOverflow) {
                            val lineEndIndex = textLayoutResult.getLineEnd(
                                lineIndex = 0,
                                visibleEnd = true
                            )
                            remainText.value = locations
                                .substring(lineEndIndex)
                            additional = locations
                                .substring(lineEndIndex)
                                .count { it == ',' }
                        }
                    },
                )
            }

            if (additional != 0) {
                CounterBadge(additionalLocationsCount = additional)
            }
        }
        Text(text = "Cut text is: "+ remainText.value)
    }
}

@Composable
private fun CounterBadge(additionalLocationsCount: Int) {
    Box(
        modifier = Modifier
            .padding(start = 8.dp)
            .size(32.dp)
            .clip(RoundedCornerShape(100))
            .background(Color.Green.copy(alpha = 0.3f))
    ) {
        Text(
            text = "+$additionalLocationsCount",
            overflow = TextOverflow.Visible,
            maxLines = 1,
            modifier = Modifier
                .align(Alignment.Center),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MaterialTheme {
        Card(
            Modifier.padding(16.dp),
            border = BorderStroke(1.dp, Color.Green),
        ) {
            LocationLabelArea(
                locations = "Some location, Some, Some location, Some location, Some location"
            )
        }
    }
}