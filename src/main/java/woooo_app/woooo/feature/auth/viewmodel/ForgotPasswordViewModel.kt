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
import woooo_app.woooo.data.models.auth.requestmodels.ForgotPasswordRequestModel
import woooo_app.woooo.domain.usecase.ForgotPasswordUseCase
import woooo_app.woooo.feature.auth.GV
import woooo_app.woooo.shared.base.doOnFailure
import woooo_app.woooo.shared.base.doOnLoading
import woooo_app.woooo.shared.base.doOnSuccess
import woooo_app.woooo.shared.components.showToast
import woooo_app.woooo.utils.Validators
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val forgotPasswordUseCase: ForgotPasswordUseCase) :
    ViewModel() {

    private val _forgotPasswordState: MutableState<ForgotPasswordState> = mutableStateOf(
        ForgotPasswordState()
    )
    val forgotPasswordState: State<ForgotPasswordState> = _forgotPasswordState

    //for error show
    private val _setErrorEmailController = mutableStateOf(false)
    val getErrorEmailController: State<Boolean> = _setErrorEmailController
    fun setErrorValueForEmail(value: Boolean) {
        _setErrorEmailController.value = value
    }

// for Error Text

    private val _setErrorText = mutableStateOf("")
    val getErrorText: State<String> = _setErrorText
    fun setErrorTextValue(value: String) {
        _setErrorText.value = value
    }

    fun validateEmail(): Boolean {
        if (GV.getEmail.value.trim() == "") {
            // pass error text to show below in text field
            setErrorTextValue(Strings.enterEmailText)
            // enabled value of error in text field
            setErrorValueForEmail(true)
            return false
        }
        if (!Validators.isValidEmail(GV.getEmail.value.trim())) {
            // pass error text to show below in text field
            setErrorTextValue(Strings.entrVldEml)
            // enabled value of error in text field
            setErrorValueForEmail(true)
            return false
        }
        return true
    }

    fun forgotPassword(context: Context) = viewModelScope.launch {
        forgotPasswordUseCase.invoke(
            ForgotPasswordRequestModel(email = GV.getEmail.value.trim())
        ).doOnSuccess {
            _forgotPasswordState.value.apply {
                data = it
                message = it.Message.toString()
                isSucceed.value = it.Success ?: false
                isLoading.value = false
            }
            Log.d("Forgot Password Success","Forgot Pass")
        }.doOnFailure {
            _forgotPasswordState.value.apply {
                message = it?.Message.toString()
                isLoading.value = it?.Success ?: false
                isFailed.value = true
            }

            Log.d("Forgot Password Failure",it?.Message.toString())
            showToast(it?.Message.toString(),context = context)

        }.doOnLoading {
            _forgotPasswordState.value.apply {
                isLoading.value = true
            }
        }.collect {}
    }


}