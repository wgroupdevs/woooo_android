package com.wgroup.woooo_app.woooo.feature.wallet.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.wgroup.woooo_app.woooo.utils.Strings
import javax.inject.Inject

class SendCurrencyViewModel @Inject constructor() : ViewModel() {

    // controller value
    private val _setButtonState = mutableStateOf(true)
    val getButtonState: State<Boolean> = _setButtonState

    fun setButtonStateValue(value: Boolean) {
        _setButtonState.value = value
    }

    // error show bool
    private val _setAddressErrorState = mutableStateOf(true)
    val getAddressErrorState: State<Boolean> = _setAddressErrorState

    fun setAddressErrorStateValue(value: Boolean) {
        _setAddressErrorState.value = value
    }

    // Error Text for All
    private val _setErrorText = mutableStateOf("")
    val getErrorText: State<String> = _setErrorText

    fun setErrorTextValue(value: String) {
        _setErrorText.value = value
    }

    private val _setAddressController = mutableStateOf("")
    val getAddressController: State<String> = _setAddressController

    fun setAddressControllerValue(value: String) {
        _setAddressController.value = value
    }

    private val _setAmountController = mutableStateOf("")
    val getAmountController: State<String> = _setAmountController

    fun setAmountControllerValue(value: String) {
        _setAmountController.value = value
    }

    private val _setAmountErrorState = mutableStateOf(true)
    val getAmountErrorState: State<Boolean> = _setAmountErrorState

    fun setAmountErrorStateValue(value: Boolean) {
        _setAmountErrorState.value = value
    }

    fun validateSendCurrencyFields(): Boolean {
        if (getAddressController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorTextValue(Strings.enterAddressText)
            // enabled value of error in text field
            setAddressErrorStateValue(true)
            return false
        }
        if (getAmountController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorTextValue(Strings.entrAmountText)
            // enabled value of error in text field
            setAmountErrorStateValue(true)
            return false
        }
        return true
    }

}