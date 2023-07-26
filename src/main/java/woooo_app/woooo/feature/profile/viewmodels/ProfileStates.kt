package woooo_app.woooo.feature.profile.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import woooo_app.woooo.data.models.profile.UpdateProfileModel
import woooo_app.woooo.data.models.profile.UploadProfileModel

// Profile States
data class UpdateProfileStates(
    var data: UpdateProfileModel = UpdateProfileModel(),
    var message: String = "",
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val isSucceed: MutableState<Boolean> = mutableStateOf(false),
    val isFailed: MutableState<Boolean> = mutableStateOf(false)
)

// upload profile
data class UploadProfileStates(
    var data: UploadProfileModel = UploadProfileModel(),
    var message: String = "",
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val isSucceed: MutableState<Boolean> = mutableStateOf(false),
    val isFailed: MutableState<Boolean> = mutableStateOf(false)
)