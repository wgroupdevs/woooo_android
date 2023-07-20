package woooo_app.woooo.feature.auth.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConfirmAccountViewModel @Inject constructor() : ViewModel() {
    private val _setOTP = mutableStateOf("")
    val getOTP: State<String> = _setOTP

    fun setOTPValue(value: String) {
        _setOTP.value = value
    }
}