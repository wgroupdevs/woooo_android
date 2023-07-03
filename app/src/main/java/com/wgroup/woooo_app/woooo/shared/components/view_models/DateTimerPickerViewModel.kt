package com.wgroup.woooo_app.woooo.shared.components.view_models

//import androidx.compose.runtime.State
//import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.ViewModel
//import com.maxkeppeker.sheets.core.models.base.UseCaseState
//import java.time.LocalDate
//import javax.inject.Inject
//
//class DateTimerPickerViewModel @Inject constructor() : ViewModel() {
//
//    private val _setDatePickerShow = mutableStateOf(UseCaseState())
//
//    /// for get value from View Model
//    val getDatePickerShow: UseCaseState = _setDatePickerShow.value
//
//    /// for set value from View Model
//    fun setDatePickerShowValue(value: UseCaseState) {
//        _setDatePickerShow.value = value
//    }
//
//    private val _setDatePickerText = mutableStateOf("")
//
//    /// for get value from View Model
//    val getDatePickerText: State<String> = _setDatePickerText
//
//    /// for set value from View Model
//    fun setDateTextValue(isEnabled: List<LocalDate>) {
//        _setDatePickerText.value = isEnabled.toString()
//    }
//
//}