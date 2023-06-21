package com.wgroup.woooo_app.woooo.feature.auth.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ForgotPasswordViewModel @Inject constructor() : ViewModel(){

    private val _enabledState = mutableStateOf(false)
    val enabledState: State<Boolean> = _enabledState

    fun setEnabledState(isEnabled: Boolean) {
        _enabledState.value = isEnabled
    }
}