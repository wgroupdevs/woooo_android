package com.wgroup.woooo_app.woooo.feature.meeting.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.theme.WooColor

@Composable
fun MeetingScheduleTab() {

    fun inputDate(): MutableList<ScheduledDate> {
        val listOfDates = mutableListOf<ScheduledDate>()

        listOfDates.add(ScheduledDate("Fri,5/19"))
        listOfDates.add(ScheduledDate("Fri,5/19"))
        listOfDates.add(ScheduledDate("Fri,5/19"))
        return listOfDates
    }
    LazyColumn(content = {
        items(inputDate()) { item ->
            ScheduledDateList(date = item.date)

        }
    })
}

@Composable
fun ScheduledDateList(date: String) {

    Column {
        VerticalSpacer()
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .background(WooColor.textBox)
        ) {
            Text(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.Center),text = date
            )
        }
    }
}

data class ScheduledDate(
    val date: String
)

