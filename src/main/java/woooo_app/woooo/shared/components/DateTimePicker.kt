package com.wgroup.woooo_app.woooo.shared.components

import android.graphics.drawable.shapes.Shape
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.marosseleng.compose.material3.datetimepickers.date.domain.DatePickerDefaults
import com.marosseleng.compose.material3.datetimepickers.date.domain.DatePickerShapes
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog
import com.marosseleng.compose.material3.datetimepickers.time.domain.TimePickerDefaults
import com.marosseleng.compose.material3.datetimepickers.time.domain.TimePickerStroke
import com.marosseleng.compose.material3.datetimepickers.time.domain.noSeconds
import com.marosseleng.compose.material3.datetimepickers.time.ui.dialog.TimePickerDialog
import com.wgroup.woooo_app.woooo.shared.components.view_models.DateTimerPickerViewModel
import com.wgroup.woooo_app.woooo.theme.WooColor
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomDateTimePicker() {

    val dateTimePickerViewModel: DateTimerPickerViewModel = hiltViewModel()

    DatePickerDialog(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .alpha(0.7f),
        containerColor = WooColor.dark,
        onDismissRequest = { },
        onDateChange = {
            val date = LocalDate.parse(it.toString())
            dateTimePickerViewModel.setDateTextValue(date)
            dateTimePickerViewModel.setDateDialogueValue(false)
        },
        buttonColors = ButtonDefaults.textButtonColors(
            contentColor = WooColor.dark,
        ),
        title = {
            Text(
                text = "SELECT DATE",color = WooColor.white,fontWeight = FontWeight.Bold
            )
        },
        colors = DatePickerDefaults.colors(
            todayLabelTextColor = WooColor.white,
            monthDayLabelSelectedBackgroundColor = WooColor.primary,
            monthDayLabelSelectedTextColor = WooColor.white,
            selectedYearTextColor = WooColor.white,
            selectedMonthTextColor = WooColor.white
        ),
//        shapes =DatePickerDefaults.shapes(year = ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomTimePicker() {
    TimePickerDialog(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .alpha(0.8f),
        containerColor = WooColor.textFieldBackGround,
        initialTime = LocalTime.now().noSeconds(),
        onTimeChange = {},
        onDismissRequest = {},
        title = { Text(text = "SELECT TIME",color = WooColor.white,fontWeight = FontWeight.Bold) },
        colors = TimePickerDefaults.colors(
            clockDigitsSelectedBackgroundColor = WooColor.selectedButtonColor,
            clockDigitsSelectedTextColor = WooColor.white,
            clockDigitsUnselectedBackgroundColor = WooColor.primary,
            clockDigitsUnselectedBorderStroke = TimePickerStroke(
                thickness = 5.dp,color = WooColor.textFieldBackGround
            ),
            amPmSwitchFieldSelectedBackgroundColor = WooColor.selectedButtonColor,
            amPmSwitchFieldSelectedTextColor = WooColor.white,
            dialCenterColor = WooColor.dark,
            dialHandColor = WooColor.dark,
            dialNumberSelectedBackgroundColor = WooColor.dark,
            dialNumberSelectedTextColor = WooColor.white
        ),
    )
}