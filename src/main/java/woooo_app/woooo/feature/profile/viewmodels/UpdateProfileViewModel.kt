package woooo_app.woooo.feature.profile.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wgroup.woooo_app.woooo.utils.Strings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import woooo_app.woooo.data.datasource.local.UserPreferences
import woooo_app.woooo.data.models.profile.UpdateProfileRequestModel
import woooo_app.woooo.domain.usecase.UpdateProfileUseCase
import woooo_app.woooo.domain.usecase.UploadProfileUseCase
import woooo_app.woooo.shared.base.doOnFailure
import woooo_app.woooo.shared.base.doOnLoading
import woooo_app.woooo.shared.base.doOnSuccess
import woooo_app.woooo.shared.components.showToast
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val uploadProfileUseCase: UploadProfileUseCase,
) : ViewModel() {
    private val _updateProfileStates: MutableState<UpdateProfileStates> =
        mutableStateOf(UpdateProfileStates())
    val updateProfileStates: State<UpdateProfileStates> = _updateProfileStates
    private val _uploadProfileStates: MutableState<UploadProfileStates> =
        mutableStateOf(UploadProfileStates())
    val uploadProfileStates: State<UploadProfileStates> = _uploadProfileStates

    // about name
    private val _setAboutController = mutableStateOf("")
    val getAboutController: State<String> = _setAboutController
    fun setAboutControllerValue(value: String) {
        _setAboutController.value = value
    }

    //  error
    private val _setAboutError = mutableStateOf(false)
    val getAboutError: State<Boolean> = _setAboutError
    fun setAboutErrorValue(value: Boolean) {
        _setAboutError.value = value
    }

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

    // Date Of Birth
    private val _setDOBController = mutableStateOf("")
    val getDOBController: State<String> = _setDOBController
    fun setDOBControllerValue(value: String) {
        _setDOBController.value = value
    }

    //  error
    private val _setDOBError = mutableStateOf(false)
    val getDOBError: State<Boolean> = _setDOBError
    fun setDOBErrorValue(value: Boolean) {
        _setDOBError.value = value
    }

    // Address
    private val _setAddressController = mutableStateOf("")
    val getAddressController: State<String> = _setAddressController
    fun setAddressControllerValue(value: String) {
        _setAddressController.value = value
    }

    //  error
    private val _setAddressError = mutableStateOf(false)
    val getAddressError: State<Boolean> = _setAddressError
    fun setAddressErrorValue(value: Boolean) {
        _setAddressError.value = value
    }

    // PostalCode
    private val _setPostalCodeController = mutableStateOf("")
    val getPostalCodeController: State<String> = _setPostalCodeController
    fun setPostalCodeControllerValue(value: String) {
        _setPostalCodeController.value = value
    }

    //  error
    private val _setPostalCodeError = mutableStateOf(false)
    val getPostalCodeError: State<Boolean> = _setPostalCodeError
    fun setPostalCodeErrorValue(value: Boolean) {
        _setPostalCodeError.value = value
    }

    // set profile image of user
    var profileImage = mutableStateOf<String>(value = "")
    fun setUserProfile() = viewModelScope.launch {
        profileImage.value = userPreferences.getProfileImage()
        Log.d(profileImage.value,"UserProfile Image")
    }

    //upload profile
    fun uploadProfile(context: Context,fileUri: Uri) = viewModelScope.launch {
        val file = File(fileUri.path!!)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        uploadProfileUseCase.invoke(params = requestFile).doOnSuccess {
            _uploadProfileStates.value.apply {
                data = it
                message = it.Message.toString()
                isSucceed.value = it.Success ?: false
                isLoading.value = false
            }
            Log.d("Upload Profile Success",it.Message.toString())
        }.doOnFailure {
            _uploadProfileStates.value.apply {
                message = it?.Message.toString()
                isLoading.value = it?.Success ?: false
                isFailed.value = true
            }

            Log.d("Upload Profile Failure",it?.Message.toString())
            showToast(it?.Message.toString(),context = context)

        }.doOnLoading {
            _uploadProfileStates.value.apply {
                isLoading.value = true
            }
        }.collect {}
    }

    //     update profile api function
    fun updateProfile(context: Context) = viewModelScope.launch {
        updateProfileUseCase.invoke(
            UpdateProfileRequestModel(
                firstName = getNameController.value,
                lastName = getLastNameController.value,
                imageURL = "",
                dateOfBirth = getDOBController.value,
                language = "userPreferences.getUserLanguage",
                description = getAboutController.value + "T11:39:08.478Z",
                address1 = getAddressController.value,
                city = "userPreferences.getUserAddress",
                country = "userPreferences.getUserCountry",
                zipCode = getPostalCodeController.value,
                state = "userPreferences.getUserState",
                languageCode = "userPreferences.getUserLanguageCode"
            )
        ).doOnSuccess {
            _updateProfileStates.value.apply {
                data = it
                message = it.Message.toString()
                isSucceed.value = it.Success ?: false
                isLoading.value = false
            }
            Log.d("Update Profile Success",it.Message.toString())
        }.doOnFailure {
            _updateProfileStates.value.apply {
                message = it?.Message.toString()
                isLoading.value = it?.Success ?: false
                isFailed.value = true
            }

            Log.d("Update Profile Failure",it?.Message.toString())
            showToast(it?.Message.toString(),context = context)

        }.doOnLoading {
            _updateProfileStates.value.apply {
                isLoading.value = true
            }
        }.collect {}
    }

    fun validateSignUpFields(): Boolean {
        if (getAboutController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorValueText(Strings.enterAboutText)
            // enabled value of error in text field
            setAboutErrorValue(true)
            return false
        } else if (getNameController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorValueText(Strings.plzEntrFirstName)
            // enabled value of error in text field
            setNameErrorValue(true)
            return false
        } else if (getLastNameController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorValueText(Strings.plzEntrLstName)
            // enabled value of error in text field
            setLastNameErrorValue(true)
            return false
        } else if (getDOBController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorValueText(Strings.enterDobTex)
            // enabled value of error in text field
            setDOBErrorValue(true)
            return false
        } else if (getAddressController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorValueText(Strings.enterAddressText)
            // enabled value of error in text field
            setAddressErrorValue(true)
            return false
        } else if (getPostalCodeController.value.trim() == "") {
            // pass error text to show below in text field
            setErrorValueText(Strings.entrPstlCodeText)
            // enabled value of error in text field
            setPostalCodeErrorValue(true)
            return false
        }
        return true
    }
}
