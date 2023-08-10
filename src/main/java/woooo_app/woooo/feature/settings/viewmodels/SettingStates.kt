package woooo_app.woooo.feature.settings.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import woooo_app.woooo.data.models.settings.ChangeNumberModel
import woooo_app.woooo.data.models.settings.ChangePasswordModel
import woooo_app.woooo.data.models.settings.DeleteAccountModel

class ChangeNumberState(
    var data: ChangeNumberModel = ChangeNumberModel(),
    var message: MutableState<String> = mutableStateOf(""),
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val isSucceed: MutableState<Boolean> = mutableStateOf(false),
    val isFailed: MutableState<Boolean> = mutableStateOf(false)
)

class ChangePasswordState(
    var data: ChangePasswordModel = ChangePasswordModel(),
    var message: MutableState<String> = mutableStateOf(""),
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val isSucceed: MutableState<Boolean> = mutableStateOf(false),
    val isFailed: MutableState<Boolean> = mutableStateOf(false)
)

class DeleteAccountState(
    var data: DeleteAccountModel = DeleteAccountModel(),
    var message: MutableState<String> = mutableStateOf(""),
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val isSucceed: MutableState<Boolean> = mutableStateOf(false),
    val isFailed: MutableState<Boolean> = mutableStateOf(false)
)