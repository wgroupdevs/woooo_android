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
import eu.siacs.conversations.http.model.UserBasicInfo
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import woooo_app.woooo.data.models.auth.requestmodels.ProfilePicRequestParams
import woooo_app.woooo.data.models.profile.UpdateProfileRequestModel
import woooo_app.woooo.domain.usecase.UpdateProfileUseCase
import woooo_app.woooo.domain.usecase.UploadProfileUseCase
import woooo_app.woooo.shared.UriToFile
import woooo_app.woooo.shared.base.doOnFailure
import woooo_app.woooo.shared.base.doOnLoading
import woooo_app.woooo.shared.base.doOnSuccess
import woooo_app.woooo.shared.components.showToast
import woooo_app.woooo.shared.components.view_models.UserPreferencesViewModel
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject


@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
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

    var getEmailController = ""
    var getPhoneController = ""

    // set profile image of user
    var profileImage = mutableStateOf(value = "")
    var language = mutableStateOf(value = "")
    var languageCode = mutableStateOf(value = "")
//    fun setUserProfile() = viewModelScope.launch {
//        profileImage.value = userPreferences.getProfileImage()
//        Log.d(profileImage.value,"UserProfile Image")
//    }

    //upload profile
    fun uploadProfile(accountId: String, context: Context, fileUri: Uri) = viewModelScope.launch {
//        var file = File(fileUri.path!!)

        Log.d("UPLOAD_PROFILE_PIC", "File URI :$fileUri")

        val file = UriToFile(context).getImageBody(fileUri)
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val imageFile: MultipartBody.Part =
            MultipartBody.Part.createFormData("Image", file.name, requestFile);

        val accountUniqueId =accountId.toRequestBody("text/plain".toMediaTypeOrNull())


        val reqstParams = ProfilePicRequestParams(
            accountUniqueId = accountUniqueId,
            imageFile = imageFile
        )

//        builder.addFormDataPart(
//            "AccountUniqueId", "0D163635-2ED2-4C36-8EBB-77EF90C74023"
//        ) // whatever data you will pass to the the request body
//            .addFormDataPart("Image", file.name, requestFile) // the profile photo
//        // make sure the name (ie profile_photo), matches your api, that is name of the key.
//
//
//        val requestBody: RequestBody = builder.build()

        uploadProfileUseCase.invoke(params = reqstParams).doOnSuccess {

            _uploadProfileStates.value.apply {
                data = it
                message = it.Message.toString()
                isSucceed.value = it.Success ?: false
                isLoading.value = false
            }
            Log.d("Upload Profile Success", it.Message.toString())
        }.doOnFailure {
            _uploadProfileStates.value.apply {
                message = it?.Message.toString()
                isLoading.value = it?.Success ?: false
                isFailed.value = true
            }

            Log.d("Upload Profile Failure", it?.Message.toString())
            showToast(it?.Message.toString(), context = context)

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
                dateOfBirth = getDOBController.value,
                language = language.value,
                description = getAboutController.value,
                address1 = getAddressController.value,
                city = "userPreferences.getUserAddress",
                country = "userPreferences.getUserCountry",
                zipCode = getPostalCodeController.value,
                state = "userPreferences.getUserState",
                languageCode = languageCode.value
            )
        ).doOnSuccess {
            _updateProfileStates.value.apply {
                data = it
                message = it.Message.toString()
                isSucceed.value = it.Success ?: false
                isLoading.value = false
            }
            Log.d("Update Profile Success",it.Message.toString())

            Log.d("Update Profile Success", it.Message.toString())
        }.doOnFailure {
            _updateProfileStates.value.apply {
                message = it?.Message.toString()
                isLoading.value = it?.Success ?: false
                isFailed.value = true
            }

            Log.d("Update Profile Failure", it?.Message.toString())
            showToast(it?.Message.toString(), context = context)

        }.doOnLoading {
            _updateProfileStates.value.apply {
                isLoading.value = true
            }
        }.collect {}

    }

    fun validateUpdateProfileFields(): Boolean {
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
        } else if (getDOBController.value.isEmpty()) {
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

    suspend fun saveUserInfoToPreferencesOnUpdateProfile(
        userPreferences: UserPreferencesViewModel, user: UserBasicInfo
    ) {
        userPreferences.setEmail(user.email ?: "")
        userPreferences.setFirstName(user.firstName ?: "")
        userPreferences.setLastName(user.lastName ?: "")
        userPreferences.setPhone(user.phoneNumber ?: "")
        userPreferences.setProfileImage(user.imageURL ?: "")
        userPreferences.setJID(user.jid ?: "")
        userPreferences.setAbout(user.about ?: "")
        userPreferences.setAddress(user.address ?: "")
        userPreferences.setPostalCode(user.postalCode ?: "")
        userPreferences.setLanguage(user.language ?: "")
        userPreferences.setLanguageCode(user.languageCode ?: "")
        userPreferences.setDOB(user.dob ?: "")


    }
}

fun uriToByteArray(context: Context,imageUri: Uri): ByteArray? {
    var inputStream: InputStream? = null
    var byteArray: ByteArray? = null

    try {
        // Open an input stream from the image URI
        inputStream = context.contentResolver.openInputStream(imageUri)

        if (inputStream != null) {
            // Convert the input stream to a byte array
            byteArray = getBytesFromInputStream(inputStream)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        inputStream?.close()
    }

    return byteArray
}

fun getBytesFromInputStream(inputStream: InputStream): ByteArray {
    // Read the input stream and write the data to a ByteArrayOutputStream
    val buffer = ByteArray(8192)
    val outputStream = ByteArrayOutputStream()

    var bytesRead: Int
    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
        outputStream.write(buffer,0,bytesRead)
    }

    // Convert the ByteArrayOutputStream to a byte array
    return outputStream.toByteArray()
}
