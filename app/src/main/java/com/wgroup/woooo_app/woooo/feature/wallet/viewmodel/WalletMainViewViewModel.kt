package com.wgroup.woooo_app.woooo.feature.wallet.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WalletMainViewViewModel @Inject constructor() : ViewModel() {

    private val _setCurrencyPopUp = mutableStateOf(false)
    val getCurrencyPopUp: State<Boolean> = _setCurrencyPopUp
    fun setCurrencyPopUpValue(value: Boolean) {
        _setCurrencyPopUp.value = value
    }

    private val _setCurrentValueOption = mutableStateOf("USD")
    val getCurrentValueOption: State<String> = _setCurrentValueOption

    fun setCurrentValueOption(value: String) {
        _setCurrentValueOption.value = value
    }
}