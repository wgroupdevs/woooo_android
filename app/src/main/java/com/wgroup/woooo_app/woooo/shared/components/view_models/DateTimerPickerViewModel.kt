package com.wgroup.woooo_app.woooo.shared.components.view_models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import javax.inject.Inject

class DateTimerPickerViewModel @Inject constructor() : ViewModel() {

    private val _setDateDialogueShow = mutableStateOf(false)
    val getDateDialogueShow: State<Boolean> = _setDateDialogueShow
    fun setDateDialogueValue(value: Boolean) {
        _setDateDialogueShow.value = value
    }

    private val _setDatePickerText = mutableStateOf("")

    /// for get value from View Model
    val getDatePickerText: State<String> = _setDatePickerText

    /// for set value from View Model
    fun setDateTextValue(isEnabled: LocalDate) {
        _setDatePickerText.value = isEnabled.toString()
    }

}