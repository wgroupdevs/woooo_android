package woooo_app.woooo.feature.settings.viewmodels.security

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import woooo_app.woooo.data.models.settings.requestModels.ChangePasswordReqModel
import woooo_app.woooo.domain.usecase.settings.ChangePasswordUseCase
import woooo_app.woooo.feature.settings.viewmodels.ChangePasswordState
import woooo_app.woooo.shared.base.doOnFailure
import woooo_app.woooo.shared.base.doOnLoading
import woooo_app.woooo.shared.base.doOnSuccess
import woooo_app.woooo.shared.components.showToast
import javax.inject.Inject

@HiltViewModel
class SecurityViewModel @Inject constructor(private val changePasswordUseCase: ChangePasswordUseCase) :
    ViewModel() {

    private val _changePasswordState: MutableState<ChangePasswordState> =
        mutableStateOf(ChangePasswordState())
    val changePasswordState: State<ChangePasswordState> = _changePasswordState

    // newPassword
    private val _setNewPassword = mutableStateOf("")
    val getNewPassword: State<String> = _setNewPassword
    fun setNewPasswordControllerValue(value: String) {
        _setNewPassword.value = value
    }

    //  error
    private val _setNewPasswordError = mutableStateOf(false)
    val getNewPasswordError: State<Boolean> = _setNewPasswordError
    fun setNewPasswordErrorValue(value: Boolean) {
        _setNewPasswordError.value = value
    }

    // password
    private val _setCurrentPasswordController = mutableStateOf("")
    val getCurrentPasswordController: State<String> = _setCurrentPasswordController
    fun setCurrentPasswordControllerValue(value: String) {
        _setCurrentPasswordController.value = value
    }

    //  error
    private val _setCurrentPasswordError = mutableStateOf(false)
    val getCurrentPasswordError: State<Boolean> = _setCurrentPasswordError
    fun setCurrentPasswordErrorValue(value: Boolean) {
        _setCurrentPasswordError.value = value
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

    // Error Text
    private val _setErrorText = mutableStateOf("")
    val getErrorText: State<String> = _setErrorText
    fun setErrorTextValue(value: String) {
        _setErrorText.value = value
    }

    fun validateFields(): Boolean {
        if (getNewPassword.value.isEmpty()) {
            setNewPasswordErrorValue(true)
            setErrorTextValue("Enter Number")
            return false
        }
        if (getConfirmPasswordController.value.isEmpty()) {
            setConfirmPasswordErrorValue(true)
            setErrorTextValue("Enter Confirm Password")
            return false
        }
        if (getCurrentPasswordController.value.isEmpty()) {
            setCurrentPasswordErrorValue(true)
            setErrorTextValue("Enter Current Password")
            return false
        }
        if (getNewPassword.value != getConfirmPasswordController.value) {
            setNewPasswordErrorValue(true)
            setConfirmPasswordErrorValue(true)
            setErrorTextValue("Password Must Be Same")
            return false
        }
        return true
    }

    fun changeNumber(accountId: String,context: Context) = viewModelScope.launch {
        changePasswordUseCase.invoke(
            ChangePasswordReqModel(
                accountId = accountId,
                currentPassword = getCurrentPasswordController.value,
                newPassword = getNewPassword.value
            )
        ).doOnSuccess {
            _changePasswordState.value.apply {
                data = it
                message.value = it.Message.toString()
                isSucceed.value = it.Success ?: false
                isLoading.value = false
            }
            showToast(changePasswordState.value.message.value,context)
        }.doOnFailure {
            _changePasswordState.value.apply {
                message.value = it?.Message.toString()
                isLoading.value = it?.Success ?: false
                isFailed.value = true
            }
//            setPasswordErrorValue(true)
//            setErrorValueText("Please Enter Valid Password")
            showToast(changePasswordState.value.message.value,context)
        }.doOnLoading {
            _changePasswordState.value.apply {
                isLoading.value = true
            }
        }.collect {}
    }

}