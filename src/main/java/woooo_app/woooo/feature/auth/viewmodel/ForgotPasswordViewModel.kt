package com.wgroup.woooo_app.woooo.feature.auth.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.wgroup.woooo_app.woooo.utils.Strings
import woooo_app.woooo.utils.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotViewModel @Inject constructor() : ViewModel() {

    // for input in text field
    private val _setEmailController = mutableStateOf("")
    val getEmailController: State<String> = _setEmailController
    fun setEmailControllerValue(value: String) {
        _setEmailController.value = value
    }

    //for error show
    private val _setErrorEmailController = mutableStateOf(false)
    val getErrorEmailController: State<Boolean> = _setErrorEmailController
    fun setErrorValueForEmail(value: Boolean) {
        _setErrorEmailController.value = value
    }
// for Error Text

    private val _setErrorText = mutableStateOf("")
    val getErrorText: State<String> = _setErrorText
    fun setErrorTextValue(value: String) {
        _setErrorText.value = value
    }

    fun validateEmail(): Boolean {
        if (getEmailController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorTextValue(Strings.enterEmailText)
            // enabled value of error in text field
            setErrorValueForEmail(true)
            return false
        }
        if (!Validators.isValidEmail(getEmailController.value.trim())) {
            // pass error text to show below in text field
            setErrorTextValue(Strings.entrVldEml)
            // enabled value of error in text field
            setErrorValueForEmail(true)
            return false
        }
        return true
    }
}