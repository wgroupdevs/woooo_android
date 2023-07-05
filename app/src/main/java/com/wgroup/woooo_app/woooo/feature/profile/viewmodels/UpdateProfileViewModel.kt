package com.wgroup.woooo_app.woooo.feature.profile.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.wgroup.woooo_app.woooo.utils.Strings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor() : ViewModel() {

    // about name
    private val _setAboutController = mutableStateOf("")
    val getAboutController: State<String> = _setAboutController
    fun setAboutControllerValue(value: String) {
        _setAboutController.value = value
    }

    //  error
    private val _setAboutError = mutableStateOf(false)
    val getAboutError: State<Boolean> = _setAboutError
    fun setAboutErrorValue(value: Boolean) {
        _setAboutError.value = value
    }

    // first name
    private val _setNameController = mutableStateOf("")
    val getNameController: State<String> = _setNameController
    fun setNameControllerValue(value: String) {
        _setNameController.value = value
    }

    //  error
    private val _setNameError = mutableStateOf(false)
    val getNameError: State<Boolean> = _setNameError
    fun setNameErrorValue(value: Boolean) {
        _setNameError.value = value
    }

    // error text for All Fields
    private val _setErrorText = mutableStateOf("")
    val getErrorText: State<String> = _setErrorText
    fun setErrorValueText(value: String) {
        _setErrorText.value = value
    }

    // last name
    private val _setLastNameController = mutableStateOf("")
    val getLastNameController: State<String> = _setLastNameController
    fun setLastNameControllerValue(value: String) {
        _setLastNameController.value = value
    }

    //  error
    private val _setLastNameError = mutableStateOf(false)
    val getLastNameError: State<Boolean> = _setLastNameError
    fun setLastNameErrorValue(value: Boolean) {
        _setLastNameError.value = value
    }

    // Date Of Birth
    private val _setDOBController = mutableStateOf("")
    val getDOBController: State<String> = _setDOBController
    fun setDOBControllerValue(value: String) {
        _setDOBController.value = value
    }

    //  error
    private val _setDOBError = mutableStateOf(false)
    val getDOBError: State<Boolean> = _setDOBError
    fun setDOBErrorValue(value: Boolean) {
        _setDOBError.value = value
    }

    // Address
    private val _setAddressController = mutableStateOf("")
    val getAddressController: State<String> = _setAddressController
    fun setAddressControllerValue(value: String) {
        _setAddressController.value = value
    }

    //  error
    private val _setAddressError = mutableStateOf(false)
    val getAddressError: State<Boolean> = _setAddressError
    fun setAddressErrorValue(value: Boolean) {
        _setAddressError.value = value
    }

    // PostalCode
    private val _setPostalCodeController = mutableStateOf("")
    val getPostalCodeController: State<String> = _setPostalCodeController
    fun setPostalCodeControllerValue(value: String) {
        _setPostalCodeController.value = value
    }

    //  error
    private val _setPostalCodeError = mutableStateOf(false)
    val getPostalCodeError: State<Boolean> = _setPostalCodeError
    fun setPostalCodeErrorValue(value: Boolean) {
        _setPostalCodeError.value = value
    }

    fun validateSignUpFields(): Boolean {
        if (getAboutController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorValueText(Strings.enterAboutText)
            // enabled value of error in text field
            setAboutErrorValue(true)
            return false
        } else if (getNameController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorValueText(Strings.plzEntrFirstName)
            // enabled value of error in text field
            setNameErrorValue(true)
            return false
        } else if (getLastNameController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorValueText(Strings.plzEntrLstName)
            // enabled value of error in text field
            setLastNameErrorValue(true)
            return false
        } else if (getDOBController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorValueText(Strings.enterDobTex)
            // enabled value of error in text field
            setDOBErrorValue(true)
            return false
        } else if (getAddressController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorValueText(Strings.enterAddressText)
            // enabled value of error in text field
            setAddressErrorValue(true)
            return false
        } else if (getPostalCodeController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorValueText(Strings.entrPstlCodeText)
            // enabled value of error in text field
            setPostalCodeErrorValue(true)
            return false
        }
        return true
    }


}