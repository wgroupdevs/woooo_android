package woooo_app.woooo.feature.meeting.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.feature.meeting.views.MeetingHistoryTab
import com.wgroup.woooo_app.woooo.feature.meeting.views.MeetingScheduleTab
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Strings
import woooo_app.woooo.shared.base.AppBackGround
import woooo_app.woooo.shared.components.CustomIcon
import woooo_app.woooo.utils.Dimension

@Composable
fun MeetingMainView(navigator: DestinationsNavigator) {
   AppBackGround {
       Column {

           val tabs = listOf("New","History","Schedule")
           var selectedTabIndex = remember { mutableStateOf(0) }
           Column(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(10.dp)
           ) {
               Row(
                   verticalAlignment = Alignment.CenterVertically,
                   modifier = Modifier.padding(start = 5.dp,top = 5.dp)
               ) {
                   CustomIcon(
                       icon = Icons.Outlined.ArrowBack,modifier = Modifier.size(30.dp)
                   )
                   HorizontalSpacer(Dimension.dimen_20)
                   Text(text = Strings.meetingText,style = MaterialTheme.typography.titleMedium)
               }
               VerticalSpacer(Dimension.dimen_10)
               TabRow(selectedTabIndex.value,
                   containerColor = MaterialTheme.colorScheme.background,
                   indicator = { tabPositions ->
                       TabIndicator(
                           tabPositions[selectedTabIndex.value],
                       )
                   },
                   divider = {}) {
                   tabs.forEachIndexed { index,title ->
                       Tab(
                           selected = selectedTabIndex.value == index,
                           onClick = { selectedTabIndex.value = index },
                           modifier = Modifier.padding(12.dp)
                       ) {
                           Text(
                               title,
                               textAlign = TextAlign.Center,
                               style = MaterialTheme.typography.titleSmall
                           )
                       }
                   }
               }

               // Content for the selected tab
               Column(
                   modifier = Modifier.fillMaxWidth(),
                   horizontalAlignment = Alignment.CenterHorizontally
               ) {
                   when (selectedTabIndex.value) {
                       0 -> {
                           // Content for New Meeting
                           NewMeetingTabView()
                       }

                       1 -> {
                           // Content for Meeting History
                           MeetingHistoryTab()
                       }

                       2 -> {
                           // Content for Schedule
                           MeetingScheduleTab()
                       }
                   }
               }
           }
       }
   }
}

@Composable
private fun TabIndicator(
    tabPosition: TabPosition,height: Dp = 3.dp
) {
    Row(modifier = Modifier.drawWithContent {
        drawContent()
        drawLine(
            color = WooColor.white,
            start = Offset(tabPosition.left.toPx(),size.height - height.toPx()),
            end = Offset(tabPosition.right.toPx(),size.height - height.toPx()),
            strokeWidth = height.toPx(),
            cap = StrokeCap.Round
        )
    }) {}
}