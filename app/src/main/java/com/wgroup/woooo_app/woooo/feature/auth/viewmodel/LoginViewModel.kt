package com.wgroup.woooo_app.woooo.feature.auth.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wgroup.woooo_app.woooo.feature.auth.domain.model.params.LoginRequestParams
import com.wgroup.woooo_app.woooo.feature.auth.domain.usecase.LoginUseCase
import com.wgroup.woooo_app.woooo.shared.base.doOnFailure
import com.wgroup.woooo_app.woooo.shared.base.doOnLoading
import com.wgroup.woooo_app.woooo.shared.base.doOnSuccess
import com.wgroup.woooo_app.woooo.utils.Strings
import com.wgroup.woooo_app.woooo.utils.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
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
    // validate login with Email fields
    fun validateEmailPass(): Boolean {
        if (getEmailController.value == "") {
            // pass error text to show below in text field
            setErrorTextValue(Strings.enterEmailText)
            // enabled value of error in text field
            setErrorValueForEmail(true)
            return false
        }
        if (!Validators.isValidEmail(getEmailController.value)) {
            // pass error text to show below in text field
            setErrorTextValue(Strings.entrVldEml)
            // enabled value of error in text field
            setErrorValueForEmail(true)
            return false
        }

        if (getPasswordController.value == "") {
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