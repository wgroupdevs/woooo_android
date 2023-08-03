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
import woooo_app.woooo.data.models.auth.requestmodels.ConfirmAccountRequestModel
import woooo_app.woooo.data.models.auth.requestmodels.ReSentOTPRequestModel
import woooo_app.woooo.domain.usecase.ConfirmAccountUseCase
import woooo_app.woooo.domain.usecase.ReSendCodeUseCase
import woooo_app.woooo.feature.auth.GV
import woooo_app.woooo.shared.base.doOnFailure
import woooo_app.woooo.shared.base.doOnLoading
import woooo_app.woooo.shared.base.doOnSuccess
import woooo_app.woooo.shared.components.showToast
import javax.inject.Inject

@HiltViewModel
class ConfirmAccountViewModel @Inject constructor(
    private val confirmAccountUseCase: ConfirmAccountUseCase,
    private val reSendCodeUseCase: ReSendCodeUseCase
) : ViewModel() {
    // confirm account State variable
    private val _confirmAccountState: MutableState<ConfirmAccountState> =
        mutableStateOf(ConfirmAccountState())
    val confirmAccountState: State<ConfirmAccountState> = _confirmAccountState
    // resent State variable

    private val _resentCodeState: MutableState<ResentCodeState> = mutableStateOf(ResentCodeState())
    val resentCodeState: State<ResentCodeState> = _resentCodeState

    // otp field
    private val _setOTP = mutableStateOf("")
    val getOTP: State<String> = _setOTP

    fun setOTPValue(value: String) {
        _setOTP.value = value
    }

    // error text for OTP
    private val _setOtpErrorText = mutableStateOf("")
    val getOtpErrorText: State<String> = _setOtpErrorText
    fun setOtpErrorText(value: String) {
        _setOtpErrorText.value = value
    }

    // otp error state
    private val _setOTPError = mutableStateOf(false)
    val getOTPError: State<Boolean> = _setOTPError
    fun setOTPStateValue(value: Boolean) {
        _setOTPError.value = value
    }

    // Confirm Account Api Function
    fun confirmAccount(context: Context) = viewModelScope.launch {
        val params = ConfirmAccountRequestModel(
            email = GV.getEmail.value,code = getOTP.value.trim()
        )

        confirmAccountUseCase.invoke(
            params = params.toMap()
        ).doOnSuccess {
            _confirmAccountState.value.apply {
                data = it
                message = it.Message.toString()
                isSucceed.value = it.Success ?: false
                isLoading.value = false
            }
            GV.clearEmailField()
            Log.d("Confirm Account Success","Account Confirmed")
        }.doOnFailure {
            _confirmAccountState.value.apply {
                message = it?.Message.toString()
                isLoading.value = it?.Success ?: false
                isFailed.value = true
            }

            Log.d("Confirm Account Failure",it?.Message.toString())
            showToast( it?.Message.toString(),context = context)

        }.doOnLoading {
            _confirmAccountState.value.apply {
                isLoading.value = true
            }
        }.collect {}
    }
    // ResentCode Api Function

    fun resentCodeForConfirmAccount(context: Context) = viewModelScope.launch {
Log.d(GV.getEmail.value, "scnsadncaosdcsdmsdc ")
        reSendCodeUseCase.invoke(
            BaseResendCodeReqParam(
                email = ReSentOTPRequestModel(email = GV.getEmail.value),
                IsOtpForAccount = true
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

    fun validateConfirmAccountFields(): Boolean {
        if (getOTP.value == "") {
            setOtpErrorText(Strings.pleaseEnterOtp)
            setOTPStateValue(true)
            return false
        }

        return true
    }

    fun clearSignUpFields() {
        setOTPValue("")
        setOtpErrorText("")
    }
}