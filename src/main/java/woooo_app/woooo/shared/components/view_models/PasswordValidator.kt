package woooo_app.woooo.shared.components.view_models

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PasswordValidatorViewModel @Inject constructor() : ViewModel() {
    private val _eightChar = mutableStateOf(false)
    val eightChar: State<Boolean> = _eightChar

    private val _upperCase = mutableStateOf(false)
    val upperCase: State<Boolean> = _upperCase

    private val _lowerCase = mutableStateOf(false)
    val lowerCase: State<Boolean> = _lowerCase

    private val _oneNumber = mutableStateOf(false)
    val oneNumber: State<Boolean> = _oneNumber

    private val _specialChar = mutableStateOf(false)
    val specialChar: State<Boolean> = _specialChar

    var fromSignUpKey = "fromSignUp"
    var fromSecurity = "fromSecurity"
    var fromVerifyOTP = "fromVerifyOTP"

    private val _setPasswordValidatorStateForSignUp = mutableStateOf(false)
    val getPasswordValidatorStateForSignUp: State<Boolean> = _setPasswordValidatorStateForSignUp
    fun setPasswordValidatorStateForSignUp(value: Boolean) {
        _setPasswordValidatorStateForSignUp.value = value
    }

    private val _setPasswordValidatorStateForSecurity = mutableStateOf(false)
    val getPasswordValidatorStateForSecurity: State<Boolean> = _setPasswordValidatorStateForSecurity
    fun setPasswordValidatorStateForSecurity(value: Boolean) {
        _setPasswordValidatorStateForSecurity.value = value
    }

    private val _setPasswordValidatorStateVerifyOTP = mutableStateOf(false)
    val getPasswordValidatorStateVerifyOTP: State<Boolean> = _setPasswordValidatorStateVerifyOTP

    fun setPasswordValidatorStateVerifyOTP(value: Boolean) {
        _setPasswordValidatorStateVerifyOTP.value = value
    }
//
//    private val _changePasswordDialoge = mutableStateOf(false)
//    val getChangePasswordDialoge: State<Boolean> = _changePasswordDialoge
//    fun setValueOfDialoge(value: Boolean) {
//        _changePasswordDialoge.value = value
//    }

    fun passwordValidator(it: String,from: String): Boolean {
        _eightChar.value = it.length >= 8
        Log.d(
            "" + eightChar.value.toString(),"asoaspcmckaa"
        )
        _upperCase.value = Regex("[A-Z]").containsMatchIn(it)
        _lowerCase.value = Regex("[a-z]").containsMatchIn(it)
        _oneNumber.value = Regex("[0-9]").containsMatchIn(it)
        _specialChar.value = Regex("^(.*?[$&+,:;/=?@#|'<>.^*()_%!-])").containsMatchIn(it)
        Log.d(
            ("" + (eightChar.value && upperCase.value && lowerCase.value && oneNumber.value && specialChar.value).toString()),
            "asoaspcmck"
        )
        if (eightChar.value && upperCase.value && lowerCase.value && oneNumber.value && specialChar.value) {

            when (from) {
                fromSignUpKey -> setPasswordValidatorStateForSignUp(false)
                fromSecurity -> setPasswordValidatorStateForSecurity(false)
                fromVerifyOTP -> setPasswordValidatorStateVerifyOTP(false)
            }
        } else {
            when (from) {
                fromSignUpKey -> setPasswordValidatorStateForSignUp(true)
                fromSecurity -> setPasswordValidatorStateForSecurity(true)
                fromVerifyOTP -> setPasswordValidatorStateVerifyOTP(true)
            }
            return false
        }
        return true
    }
}