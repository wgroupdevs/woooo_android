package com.wgroup.woooo_app.woooo.feature.meeting.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import woooo_app.woooo.shared.components.CustomIcon
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import woooo_app.woooo.shared.components.ViewDivider

@Composable
fun MeetingHistoryTab() {
    fun inputData(): MutableList<DataClass> {
        val list = mutableListOf<DataClass>()


        list.add(
            DataClass(
                id = 2378236492,startTime = "Wed,5/17,\n12:37:PM",endTime = "Mon,1/1,\n12:00:AM"
            )
        )
        list.add(
            DataClass(
                id = 2378236492,startTime = "Wed,5/17,\n12:37:PM",endTime = "Mon,1/1,\n12:00:AM"
            )
        )
        list.add(
            DataClass(
                id = 2378236492,startTime = "Wed,5/17,\n12:37:PM",endTime = "Mon,1/1,\n12:00:AM"
            )
        )

        list.add(
            DataClass(
                id = 2378236492,startTime = "Wed,5/17,\n12:37:PM",endTime = "Mon,1/1,\n12:00:AM"
            )
        )

        list.add(
            DataClass(
                id = 2378236492,startTime = "Wed,5/17,\n12:37:PM",endTime = "Mon,1/1,\n12:00:AM"
            )
        )

        list.add(
            DataClass(
                id = 2378236492,startTime = "Wed,5/17,\n12:37:PM",endTime = "Mon,1/1,\n12:00:AM"
            )
        )

        list.add(
            DataClass(
                id = 2378236492,startTime = "Wed,5/17,\n12:37:PM",endTime = "Mon,1/1,\n12:00:AM"
            )
        )

        list.add(
            DataClass(
                id = 2378236492,startTime = "Wed,5/17,\n12:37:PM",endTime = "Mon,1/1,\n12:00:AM"
            )
        )

        list.add(
            DataClass(
                id = 2378236492,startTime = "Wed,5/17,\n12:37:PM",endTime = "Mon,1/1,\n12:00:AM"
            )
        )

        list.add(
            DataClass(
                id = 2378236492,startTime = "Wed,5/17,\n12:37:PM",endTime = "Mon,1/1,\n12:00:AM"
            )
        )

        list.add(
            DataClass(
                id = 2378236492,startTime = "Wed,5/17,\n12:37:PM",endTime = "Mon,1/1,\n12:00:AM"
            )
        )

        list.add(
            DataClass(
                id = 2378236492,startTime = "Wed,5/17,\n12:37:PM",endTime = "Mon,1/1,\n12:00:AM"
            )
        )

        list.add(
            DataClass(
                id = 2378236492,startTime = "Wed,5/17,\n12:37:PM",endTime = "Mon,1/1,\n12:00:AM"
            )
        )

        list.add(
            DataClass(
                id = 2378236492,startTime = "Wed,5/17,\n12:37:PM",endTime = "Mon,1/1,\n12:00:AM"
            )
        )

        return list
    }
    Column {
        VerticalSpacer()
        ViewDivider()
        LazyColumn(content = {
            items(inputData()) { item ->
                MeetingHistoryList(
                    id = item.id,startTime = item.startTime,endTime = item.endTime
                )
            }
        })
    }

}

data class DataClass(
    val id: Long,val startTime: String,val endTime: String
)

@Composable
fun MeetingHistoryList(id: Long,startTime: String,endTime: String) {
    Column(Modifier.padding(10.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "$id",fontSize = 14.sp)
            Text(text = startTime,fontSize = 14.sp,textAlign = TextAlign.Center)
            Text(text = endTime,fontSize = 14.sp,textAlign = TextAlign.Center)
            CustomIcon(icon = Icons.Rounded.Download,modifier = Modifier.size(24.dp))

        }
        VerticalSpacer()
        ViewDivider()
    }
}