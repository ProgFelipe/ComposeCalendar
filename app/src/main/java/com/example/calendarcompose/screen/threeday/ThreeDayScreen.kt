package com.example.calendarcompose.screen.threeday

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calendarcompose.listStateProvidedByViewModel
import com.example.calendarcompose.model.DayEntity
import com.example.calendarcompose.screen.threeday.widgets.BottomEvensWidget
import com.example.calendarcompose.screen.threeday.widgets.TopEventsWidget
import kotlinx.coroutines.launch

private const val COLUMNS_PER_SCREEN = 3

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThreeDayScreen(dayList: List<DayEntity>, dayFixed: List<DayEntity>) {


    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp
    val columnWidth = screenWidth / COLUMNS_PER_SCREEN

    val stateRowX = rememberLazyListState() // State for the first Row, X
    val stateRowY = rememberLazyListState() // State for the second Row, Y
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollableState { delta ->
        scope.launch {
            stateRowX.scrollBy(-delta)
            stateRowY.scrollBy(-delta)
        }
        delta
    }

    val snappingLayout2 = remember(scrollState) { SnapLayoutInfoProvider(stateRowX) }
    val flingBehavior2 = rememberSnapFlingBehavior(snappingLayout2)

    val stateRowZ = rememberLazyListState()
    Column(
        Modifier
            .fillMaxSize()
            .scrollable(
                scrollState,
                Orientation.Horizontal,
                flingBehavior = ScrollableDefaults.flingBehavior()
            )
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = stateRowX,
            userScrollEnabled = false
        ) {
            items(dayList) {
                TopEventsWidget(it, columnWidth)
            }
        }

        LazyRow(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.LightGray),
            userScrollEnabled = false,
            state = stateRowY
        ) {
            // secondContainer(dayFixed, columnWidth, stateRowZ)
            items(dayFixed) {
                BottomEvensWidget(screenWidth)
            }
        }
    }
}

@Preview
@Composable
fun TestView() {
    val items = listStateProvidedByViewModel()
    val fixedItems = listStateProvidedByViewModel(true)

    ThreeDayScreen(items, fixedItems)
}