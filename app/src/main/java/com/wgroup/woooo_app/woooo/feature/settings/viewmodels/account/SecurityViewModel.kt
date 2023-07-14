package com.wgroup.woooo_app.woooo.feature.settings.viewmodels.account

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SecurityViewModel @Inject constructor() : ViewModel() {

    // password
    private val _setPasswordController = mutableStateOf("")
    val getPasswordController: State<String> = _setPasswordController
    fun setPasswordControllerValue(value: String) {
        _setPasswordController.value = value
    }

    //  error
    private val _setPasswordError = mutableStateOf(false)
    val getPasswordError: State<Boolean> = _setPasswordError
    fun setPasswordErrorValue(value: Boolean) {
        _setPasswordError.value = value
    }

    // Confirm password
    private val _setConfirmPasswordController = mutableStateOf("")
    val getConfirmPasswordController: State<String> = _setConfirmPasswordController
    fun setConfirmPasswordControllerValue(value: String) {
        _setConfirmPasswordController.value = value
    }

    //  error
    private val _setConfirmPasswordError = mutableStateOf(false)
    val getConfirmPasswordError: State<Boolean> = _setConfirmPasswordError
    fun setConfirmPasswordErrorValue(value: Boolean) {
        _setConfirmPasswordError.value = value
    }


}