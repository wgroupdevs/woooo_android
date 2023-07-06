package com.wgroup.woooo_app.woooo.feature.auth.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.wgroup.woooo_app.woooo.utils.Strings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VerifyOtpViewModel @Inject constructor() : ViewModel() {
    private val _setOtpCodeText = mutableStateOf("")
    val getOtpCodeText: State<String> = _setOtpCodeText
    fun setOtpText(value: String) {
        _setOtpCodeText.value = value
    }

    private val _setOtpError = mutableStateOf(false)
    val getOtpError: State<Boolean> = _setOtpError
    fun setOtpError(value: Boolean) {
        _setOtpError.value = value
    }

    private val _setNewPassText = mutableStateOf("")
    val getNewPassText: State<String> = _setNewPassText
    fun setNewPassText(value: String) {
        _setNewPassText.value = value
    }

    private val _setNewPassError = mutableStateOf(false)
    val getNewPassError: State<Boolean> = _setNewPassError
    fun setNewPassError(value: Boolean) {
        _setNewPassError.value = value
    }

    private val _setConfirmPassText = mutableStateOf("")
    val getConfirmPassText: State<String> = _setConfirmPassText
    fun setConfirmPassText(value: String) {
        _setConfirmPassText.value = value
    }

    private val _setConfirmPassError = mutableStateOf(false)
    val getConfirmPassError: State<Boolean> = _setConfirmPassError
    fun setConfirmPassError(value: Boolean) {
        _setConfirmPassError.value = value
    }

    private val _setErrorText = mutableStateOf("")
    val getErrorText: State<String> = _setErrorText
    fun setErrorText(value: String) {
        _setErrorText.value = value
    }

    fun validateOTPFields() {
        if (getOtpCodeText.value.trim() == "") {
            setErrorText(Strings.pleaseEnterOtp)
            setOtpError(true)

        } else if (getNewPassText.value.trim() == "") {
            setErrorText(Strings.plzEntrNewPass)

            setNewPassError(true)

        } else if (getConfirmPassText.value.trim() == "") {

            setErrorText(Strings.plzEntrConPass)
            setConfirmPassError(true)
        }
    }
}