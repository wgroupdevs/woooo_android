package woooo_app.woooo.feature.auth.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wgroup.woooo_app.woooo.utils.Strings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import woooo_app.woooo.data.models.auth.requestmodels.BaseResendCodeReqParam
import woooo_app.woooo.data.models.auth.requestmodels.ReSentOTPRequestModel
import woooo_app.woooo.data.models.auth.requestmodels.ResetPasswordRequestModel
import woooo_app.woooo.domain.usecase.ReSendCodeUseCase
import woooo_app.woooo.domain.usecase.ResetPasswordUseCase
import woooo_app.woooo.feature.auth.GV
import woooo_app.woooo.shared.base.doOnFailure
import woooo_app.woooo.shared.base.doOnLoading
import woooo_app.woooo.shared.base.doOnSuccess
import woooo_app.woooo.shared.components.showToast
import javax.inject.Inject

@HiltViewModel
class VerifyOtpViewModel @Inject constructor(
    private val reSendCodeUseCase: ReSendCodeUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    // resent State variable

    private val _resentCodeState: MutableState<ResentCodeState> = mutableStateOf(ResentCodeState())
    val resentCodeState: State<ResentCodeState> = _resentCodeState

    // reset State variable

    private val _resetPasswordState: MutableState<ResetPasswordState> =
        mutableStateOf(ResetPasswordState())
    val resetPasswordState: State<ResetPasswordState> = _resetPasswordState

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

    // reset password api function
    fun resetPassword(context: Context) = viewModelScope.launch {
        resetPasswordUseCase.invoke(
            ResetPasswordRequestModel(
                email = GV.getEmail.value,
                otp = getOtpCodeText.value,
                password = getNewPassText.value,
                confirmPassword = getConfirmPassText.value
            )
        ).doOnSuccess {
            _resetPasswordState.value.apply {
                data = it
                message = it.Message.toString()
                isSucceed.value = it.Success ?: false
                isLoading.value = false
            }
            Log.d("Resent Code Success","Resent Code")
        }.doOnFailure {
            _resetPasswordState.value.apply {
                message = it?.Message.toString()
                isLoading.value = it?.Success ?: false
                isFailed.value = true
            }

            Log.d("Resent Code Failure",it?.Message.toString())
            showToast( it?.Message.toString(),context = context)

        }.doOnLoading {
            _resetPasswordState.value.apply {
                isLoading.value = true
            }
        }.collect {}
    }

    // ResentCode Api Function

    fun resentCode(context: Context) = viewModelScope.launch {

        reSendCodeUseCase.invoke(
            BaseResendCodeReqParam(
                email = ReSentOTPRequestModel(email = GV.getEmail.value),
                IsOtpForAccount = false
            )
        ).doOnSuccess {
            _resentCodeState.value.apply {
                data = it
                message = it.Message.toString()
                isSucceed.value = it.Success ?: false
                isLoading.value = false
            }
            Log.d("Resent Code Success","Resent Code")
        }.doOnFailure {
            _resentCodeState.value.apply {
                message = it?.Message.toString()
                isLoading.value = it?.Success ?: false
                isFailed.value = true
            }

            Log.d("Resent Code Failure",it?.Message.toString())
            showToast( it?.Message.toString(),context = context)

        }.doOnLoading {
            _resentCodeState.value.apply {
                isLoading.value = true
            }
        }.collect {}
    }

    fun validateOTPFields(): Boolean {
        if (getOtpCodeText.value.trim() == "") {
            setErrorText(Strings.pleaseEnterOtp)
            setOtpError(true)
            return false
        } else if (getNewPassText.value.trim() == "") {
            setErrorText(Strings.plzEntrNewPass)
            setNewPassError(true)
            return false
        } else if (getConfirmPassText.value.trim() == "") {
            setErrorText(Strings.plzEntrConPass)
            setConfirmPassError(true)
            return false
        } else if (getConfirmPassText.value.trim() != getNewPassText.value.trim()) {
            setErrorText(Strings.enterSamePass)
            setConfirmPassError(true)
            setNewPassError(true)
            return false
        }

        return true
    }
}