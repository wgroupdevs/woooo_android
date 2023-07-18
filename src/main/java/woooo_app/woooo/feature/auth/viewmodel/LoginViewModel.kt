package com.wgroup.woooo_app.woooo.feature.auth.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wgroup.woooo_app.woooo.data.models.LoginRequestParams
import com.wgroup.woooo_app.woooo.domain.usecase.LoginUseCase
import com.wgroup.woooo_app.woooo.shared.base.doOnFailure
import com.wgroup.woooo_app.woooo.shared.base.doOnLoading
import com.wgroup.woooo_app.woooo.shared.base.doOnSuccess
import com.wgroup.woooo_app.woooo.utils.Strings
import com.wgroup.woooo_app.woooo.utils.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginWithEmailViewModel @Inject constructor(private val loginUseCase: LoginUseCase) :
    ViewModel() {
    private val _loginResponse: MutableState<LoginState> = mutableStateOf(LoginState())
    val loginResponse: State<LoginState> = _loginResponse

    /// get & set input of Email
    private val setEmailController = mutableStateOf("")
    val getEmailController: State<String> = setEmailController
    fun setEmailControllerValue(value: String) {
        setEmailController.value = value
    }

    private val _setErrorEmailController = mutableStateOf(false)
    val getErrorEmailController: State<Boolean> = _setErrorEmailController
    fun setErrorValueForEmail(value: Boolean) {
        _setErrorEmailController.value = value
    }

    /// get & set input of password

    private val setPasswordController = mutableStateOf("")
    val getPasswordController: State<String> = setPasswordController
    fun setPasswordControllerValue(value: String) {
        setPasswordController.value = value
    }

    private val _setErrorPasswordController = mutableStateOf(false)
    val getErrorPasswordController: State<Boolean> = _setErrorPasswordController
    fun setErrorValueForPassword(value: Boolean) {
        _setErrorPasswordController.value = value
    }

    private val _setEyeForPassword = mutableStateOf(false)
    val getEyeForPassword: State<Boolean> = _setEyeForPassword
    fun setEyeValueForPassword(value: Boolean) {
        _setEyeForPassword.value = value
    }

    private val _setErrorText = mutableStateOf("")
    val getErrorText: State<String> = _setErrorText
    fun setErrorTextValue(value: String) {
        _setErrorText.value = value
    }

    // change bool login with email to phone and vise versa

    private val _setLoginWithEmail = mutableStateOf(false)
    val getLoginWithEmail: State<Boolean> = _setLoginWithEmail
    fun setLoginWithEmailValue(value: Boolean) {
        _setLoginWithEmail.value = value
    }

    // validate login with Email fields
    fun validateEmailPass(): Boolean {
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

        if (getPasswordController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorTextValue(Strings.enterPasswordText)
            // enabled value of error in text field
            setErrorValueForPassword(true)
            return false
        }
        return true
    }

    fun login() = viewModelScope.launch {
        loginUseCase.invoke(
            LoginRequestParams(
                email = "hamzaf875@gmail.commm",deviceName = "Mobile",password = "Hamza@123"
            )
        ).doOnSuccess {
            _loginResponse.value = LoginState(
                it,
            )
        }.doOnFailure {
            _loginResponse.value = LoginState(
                error = it.toString(),
            )
        }.doOnLoading {
            _loginResponse.value = LoginState(
                isLoading = true
            )
        }.collect {}
    }


}

class LoginWithPhoneViewModel @Inject constructor() : ViewModel() {

    // country
    private val _setCountryText = mutableStateOf("")
    val getCountryText: State<String> = _setCountryText
    fun setCountryText(value: String) {
        _setCountryText.value = value
    }

    private val _setCountryError = mutableStateOf(false)
    val getCountryError: State<Boolean> = _setCountryError

    fun setCountryError(value: Boolean) {
        _setCountryError.value = value
    }

    // phone
    private val _setPhoneText = mutableStateOf("")
    val getPhoneText: State<String> = _setPhoneText
    fun setPhoneText(value: String) {
        _setPhoneText.value = value
    }

    private val _setPhoneError = mutableStateOf(false)
    val getPhoneError: State<Boolean> = _setPhoneError

    fun setPhoneError(value: Boolean) {
        _setPhoneError.value = value
    }

    // password
    private val _setPasswordText = mutableStateOf("")
    val getPasswordText: State<String> = _setPasswordText
    fun setPasswordText(value: String) {
        _setPasswordText.value = value
    }

    private val _setPasswordError = mutableStateOf(false)
    val getPasswordError: State<Boolean> = _setPasswordError

    fun setPasswordError(value: Boolean) {
        _setPasswordError.value = value
    }

    // error
    private val _setErrorText = mutableStateOf("")
    val getErrorText: State<String> = _setErrorText
    fun setErrorText(value: String) {
        _setErrorText.value = value
    }

    //  Show Country Picker
    private val _getShowCountryPicker = mutableStateOf(false)
    val setShowCountryPicker: State<Boolean> = _getShowCountryPicker
    fun setShowCountryPickerValue(value: Boolean) {
        _getShowCountryPicker.value = value
    }

    fun validateEmailWithPhoneFields() {
        if (getCountryText.value.trim() == "") {
            setErrorText(Strings.slctCountry)
            setCountryError(true)
        } else if (getPhoneText.value.trim() == "") {
            setErrorText(Strings.enterNumberText)
            setPhoneError(true)
        } else if (getPasswordText.value.trim() == "") {
            setErrorText(Strings.enterPasswordText)
            setPasswordError(true)
        }

    }
}





