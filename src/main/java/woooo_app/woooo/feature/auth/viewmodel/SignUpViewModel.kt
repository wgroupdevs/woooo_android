package woooo_app.woooo.feature.auth.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wgroup.woooo_app.woooo.utils.Strings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import woooo_app.woooo.data.models.auth.requestmodels.SignUpRequestModel
import woooo_app.woooo.domain.usecase.SignUpUseCase
import woooo_app.woooo.feature.auth.EmailForAuthModule
import woooo_app.woooo.shared.base.doOnFailure
import woooo_app.woooo.shared.base.doOnLoading
import woooo_app.woooo.shared.base.doOnSuccess
import woooo_app.woooo.utils.Validators
import woooo_app.woooo.utils.Validators.isStringContainNumeric
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpUseCase: SignUpUseCase) : ViewModel() {
    private val _signUpResponseState: MutableState<SignUpSate> = mutableStateOf(SignUpSate())
    val signUpResponseState: State<SignUpSate> = _signUpResponseState

    // first name
    private val _setNameController = mutableStateOf("")
    val getNameController: State<String> = _setNameController
    fun setNameControllerValue(value: String) {
        _setNameController.value = value
    }

    //  error
    private val _setNameError = mutableStateOf(false)
    val getNameError: State<Boolean> = _setNameError
    fun setNameErrorValue(value: Boolean) {
        _setNameError.value = value
    }

    // error text for All Fields
    private val _setErrorText = mutableStateOf("")
    val getErrorText: State<String> = _setErrorText
    fun setErrorValueText(value: String) {
        _setErrorText.value = value
    }

    // last name
    private val _setLastNameController = mutableStateOf("")
    val getLastNameController: State<String> = _setLastNameController
    fun setLastNameControllerValue(value: String) {
        _setLastNameController.value = value
    }

    //  error
    private val _setLastNameError = mutableStateOf(false)
    val getLastNameError: State<Boolean> = _setLastNameError
    fun setLastNameErrorValue(value: Boolean) {
        _setLastNameError.value = value
    }

    //  Show Country Picker
    private val _getShowCountryPicker = mutableStateOf(false)
    val setShowCountryPicker: State<Boolean> = _getShowCountryPicker
    fun setShowCountryPickerValue(value: Boolean) {
        _getShowCountryPicker.value = value
    }

//    // email
//    private val _setEmailController = mutableStateOf("")
//    val getEmailController: State<String> = _setEmailController
//    fun setEmailControllerValue(value: String) {
//        _setEmailController.value = value
//    }

    //  error
    private val _setEmailError = mutableStateOf(false)
    val getEmailError: State<Boolean> = _setEmailError
    fun setEmailErrorValue(value: Boolean) {
        _setEmailError.value = value
    }

    // password
    private val _setPasswordController = mutableStateOf("")
    val getPasswordController: State<String> = _setPasswordController
    fun setPasswordControllerValue(value: String) {
        _setPasswordController.value = value
    }

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

    // Referral code
    private val _setReferralCodeController = mutableStateOf("")
    val getReferralCodeController: State<String> = _setReferralCodeController
    fun setReferralCodeControllerValue(value: String) {
        _setReferralCodeController.value = value
    }

//    val setSuccessDialog = mutableStateOf(false)
//    val getSuccessDialog: State<Boolean> = setSuccessDialog

    // sign up
    fun signUp() = viewModelScope.launch {
        Log.d("Success Call response","${signUpResponseState.value.data}")
        signUpUseCase.invoke(
            SignUpRequestModel(
                email = EmailForAuthModule.getEmail.value.trim(),
                phoneNumber = getPhoneNumberController.value.trim(),
                password = getPasswordController.value.trim(),
                userReferralCode = getReferralCodeController.value.trim(),
                firstName = getNameController.value.trim(),
                lastName = getLastNameController.value.trim(),
                deviceId = "".trim(),
                ipAddress = "".trim()
            )
        ).doOnSuccess {

            Log.d(_signUpResponseState.value.data.Data?.email,"SUCCESS DATA EMAIL doOnSuccess")

            _signUpResponseState.value.apply {
                data = it
                message = it.Message.toString()
                isSucceed.value = it.Success ?: false
                isLoading.value = false
            }
            Log.d(_signUpResponseState.value.data.Data?.email,"SUCCESS DATA EMAIL")

        }.doOnFailure {
            Log.d(_signUpResponseState.value.data.Data?.email,"SUCCESS DATA EMAIL doOnFailure")
            _signUpResponseState.value.apply {
                message = it?.Message.toString()
                isLoading.value = it?.Success ?: false
                isFailed.value = true
            }
        }.doOnLoading {
            _signUpResponseState.value.apply {
                isLoading.value = true
            }
        }.collect {}
    }

    fun validateSignUpFields(): Boolean {
        if (getNameController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorValueText(Strings.plzEntrFirstName)
            // enabled value of error in text field
            setNameErrorValue(true)
            return false
        } else if (!isStringContainNumeric(getNameController.value)) {
            // pass error text to show below in text field
            setErrorValueText("Name Only Contain Alphabets")
            // enabled value of error in text field
            setNameErrorValue(true)
        } else if (getLastNameController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorValueText(Strings.plzEntrLstName)
            // enabled value of error in text field
            setLastNameErrorValue(true)
            return false
        } else if (EmailForAuthModule.getEmail.value.trim() == "") {
            // pass error text to show below in text field
            setErrorValueText(Strings.plzEnterEmail)
            // enabled value of error in text field
            setEmailErrorValue(true)
            return false
        } else if (!Validators.isValidEmail(EmailForAuthModule.getEmail.value.trim())) {
            // pass error text to show below in text field
            setErrorValueText(Strings.entrValidEmailText)
            // enabled value of error in text field
            setEmailErrorValue(true)
            return false
        } else if (getPasswordController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorValueText(Strings.plzEntrNewPass)
            // enabled value of error in text field
            setPasswordErrorValue(true)
            return false
        } else if (getConfirmPasswordController.value.trim() == "") {
//             pass error text to show below in text field
            setErrorValueText(Strings.plzEntrConPass)
//             enabled value of error in text field
            setConfirmPasswordErrorValue(true)
            return false
        } else if (getConfirmPasswordController.value.trim() != getPasswordController.value.trim()) {
//             pass error text to show below in text field
            setErrorValueText(Strings.enterSamePass)
//             enabled value of error in text field
            setPasswordErrorValue(true)
            setConfirmPasswordErrorValue(true)
            return false
        }
        return true
    }

    fun clearSignUpFields() {
        setShowCountryPickerValue(false)
        setReferralCodeControllerValue("")
        setPasswordControllerValue("")
        setConfirmPasswordControllerValue("")
        setPhoneNumberControllerValue("")
        setLastNameControllerValue("")
        setNameControllerValue("")
    }
}