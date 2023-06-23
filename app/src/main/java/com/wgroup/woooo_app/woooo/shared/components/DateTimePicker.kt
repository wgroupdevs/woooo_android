package com.wgroup.woooo_app.woooo.shared.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.wgroup.woooo_app.woooo.shared.components.view_models.DateTimerPickerViewModel
import java.time.LocalDate
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun DateTimePicker() {
    val dateTimeViewModel: DateTimerPickerViewModel = hiltViewModel()

//    val selectedDates = remember { mutableStateOf<List<LocalDate>>(listOf()) }
//    val disabledDates = listOf(
//        LocalDate.now().minusDays(7),
//        LocalDate.now().minusDays(12),
//        LocalDate.now().plusDays(3),
//    )
//    val celendarState = rememberUseCaseState()
    CalendarDialog(
    header = Header.Custom {
        Text(modifier = Modifier.padding(10.dp),
            text = "SELECT DATE",
            style = MaterialTheme.typography.titleSmall
        )
    },
    state = dateTimeViewModel.getDatePickerShow,
    config = CalendarConfig(
    yearSelection = true,
    monthSelection = true,
    style = CalendarStyle.MONTH,
//    disabledDates = disabledDates
    ),
    selection = CalendarSelection.Dates { dates ->
       dateTimeViewModel.setDateTextValue(dates)
    })
}