package woooo_app.woooo.feature.settings.viewmodels.account

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import woooo_app.woooo.data.models.settings.requestModels.ChangeNumberReqModel
import woooo_app.woooo.domain.usecase.settings.ChangeNumberUseCase
import woooo_app.woooo.domain.usecase.settings.DeleteAccountUseCase
import woooo_app.woooo.feature.settings.viewmodels.ChangeNumberState
import woooo_app.woooo.feature.settings.viewmodels.DeleteAccountState
import woooo_app.woooo.shared.base.doOnFailure
import woooo_app.woooo.shared.base.doOnLoading
import woooo_app.woooo.shared.base.doOnSuccess
import woooo_app.woooo.shared.components.showToast
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val changeNumberUseCase: ChangeNumberUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {

    private val _changeNumberState: MutableState<ChangeNumberState> =
        mutableStateOf(ChangeNumberState())
    val changeNumberState: State<ChangeNumberState> = _changeNumberState

    private val _deleteAccountState: MutableState<DeleteAccountState> = mutableStateOf(
        DeleteAccountState()
    )
    val deleteAccountState: State<DeleteAccountState> = _deleteAccountState

    // phone number
    private val _setPhoneNumberController = mutableStateOf("")
    val getPhoneNumberController: State<String> = _setPhoneNumberController
    fun setPhoneNumberControllerValue(value: String) {
        _setPhoneNumberController.value = value
    }

    //  error
    private val _setPasswordError = mutableStateOf(false)
    val getPasswordError: State<Boolean> = _setPasswordError
    fun setPasswordErrorValue(value: Boolean) {
        _setPasswordError.value = value
    }

    //  error
    private val _setPhoneError = mutableStateOf(false)
    val getPhoneError: State<Boolean> = _setPhoneError
    fun setPhoneError(value: Boolean) {
        _setPhoneError.value = value
    }

    //  Show Country Picker
    private val _getShowCountryPicker = mutableStateOf(false)
    val setShowCountryPicker: State<Boolean> = _getShowCountryPicker
    fun setShowCountryPickerValue(value: Boolean) {
        _getShowCountryPicker.value = value
    }

    // password
    private val _setPasswordController = mutableStateOf("")
    val getPasswordController: State<String> = _setPasswordController
    fun setPasswordControllerValue(value: String) {
        _setPasswordController.value = value
    }

    val getNumberWithCode = mutableStateOf("")

    fun chaneNumber(accountId: String,context: Context) = viewModelScope.launch {
        changeNumberUseCase.invoke(
            ChangeNumberReqModel(
                accountId = accountId,
                phoneNumber = getNumberWithCode.value.trim(),
                password = getPasswordController.value.trim()
            )
        ).doOnSuccess {
            _changeNumberState.value.apply {
                data = it
                message.value = it.Message.toString()
                isSucceed.value = it.Success ?: false
                isLoading.value = false
            }
            showToast("Successfully Change",context)
        }.doOnFailure {
            _changeNumberState.value.apply {
                message.value = it?.Message.toString()
                isLoading.value = it?.Success ?: false
                isFailed.value = true
            }
            setPasswordErrorValue(true)
            setErrorValueText("Please Enter Valid Password")
            showToast(changeNumberState.value.message.value,context)
        }.doOnLoading {
            _changeNumberState.value.apply {
                isLoading.value = true
            }
        }.collect {}
    }

    fun deleteAccount(accountId: String,context: Context) = viewModelScope.launch {
        deleteAccountUseCase.invoke(
            accountId = accountId
        ).doOnSuccess {
            _deleteAccountState.value.apply {
                data = it
                message.value = it.Message.toString()
                isSucceed.value = it.Success ?: false
                isLoading.value = false
            }

        }.doOnFailure {
            _deleteAccountState.value.apply {
                message.value = it?.Message.toString()
                isLoading.value = it?.Success ?: false
                isFailed.value = true
            }
            showToast(changeNumberState.value.message.value,context)
        }.doOnLoading {
            _deleteAccountState.value.apply {
                isLoading.value = true
            }
        }.collect {}
    }

    // error text for All Fields
    private val _setErrorText = mutableStateOf("")
    val getErrorText: State<String> = _setErrorText
    fun setErrorValueText(value: String) {
        _setErrorText.value = value
    }

    fun validateFields(): Boolean {
        if (getPhoneNumberController.value.isEmpty()) {
            setPhoneError(true)
            setErrorValueText("Please Enter Number ")
            return false
        }
        if (getPasswordController.value.isEmpty()) {
            setPasswordErrorValue(true)
            setErrorValueText("Please Enter Password ")
            return false
        }
        return true
    }
}