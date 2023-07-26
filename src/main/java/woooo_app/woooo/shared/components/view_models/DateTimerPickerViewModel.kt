package woooo_app.woooo.shared.components.view_models

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import javax.inject.Inject

class DateTimerPickerViewModel @Inject constructor() : ViewModel() {

    private val _setDateDialogueShowForUpdateProfile = mutableStateOf(false)
    val getDateDialogueShowForUpdateProfile: State<Boolean> = _setDateDialogueShowForUpdateProfile
    fun setDateDialogueValueForUpdateProfile(value: Boolean) {
        _setDateDialogueShowForUpdateProfile.value = value
    }

    private val _setDatePickerTextForUpdateProfile = mutableStateOf("")

    /// for get value from View Model
    val getDatePickerTextForUpdateProfile: State<String> = _setDatePickerTextForUpdateProfile

    /// for set value from View Model
    fun setDateTextValueForUpdateProfile(isEnabled: LocalDate) {
        _setDatePickerTextForUpdateProfile.value = isEnabled.toString()
    }
}