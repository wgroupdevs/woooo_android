package woooo_app.woooo.feature.auth.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import woooo_app.woooo.data.models.auth.LoginModel
import woooo_app.woooo.data.models.auth.SignUpModel

// login State
data class LoginState(
    val data: LoginModel = LoginModel(),
    val error: String = "",
    val isLoading: Boolean = false
)
// SignUp State

data class SignUpSate(
    var data: SignUpModel = SignUpModel(),
    var message: String = "",
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val isSucceed: MutableState<Boolean> = mutableStateOf(false),
    val isFailed: MutableState<Boolean> = mutableStateOf(false)
)