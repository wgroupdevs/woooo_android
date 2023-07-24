package woooo_app.woooo.feature.auth.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import woooo_app.woooo.data.models.auth.ConfirmAccountModel
import woooo_app.woooo.data.models.auth.ForgotPasswordModel
import woooo_app.woooo.data.models.auth.LoginModel
import woooo_app.woooo.data.models.auth.ResentCodeModel
import woooo_app.woooo.data.models.auth.ResetPasswordModel
import woooo_app.woooo.data.models.auth.SignUpModel

// login State
data class LoginState(
    val data: LoginModel = LoginModel(),val error: String = "",val isLoading: Boolean = false
)
// SignUp State

data class SignUpSate(
    var data: SignUpModel = SignUpModel(),
    var message: String = "",
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val isSucceed: MutableState<Boolean> = mutableStateOf(false),
    val isFailed: MutableState<Boolean> = mutableStateOf(false)
)

// Confirm Account State
data class ConfirmAccountState(
    var data: ConfirmAccountModel = ConfirmAccountModel(),
    var message: String = "",
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val isSucceed: MutableState<Boolean> = mutableStateOf(false),
    val isFailed: MutableState<Boolean> = mutableStateOf(false)
)

// Resent Code State
data class ResentCodeState(
    var data: ResentCodeModel = ResentCodeModel(),
    var message: String = "",
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val isSucceed: MutableState<Boolean> = mutableStateOf(false),
    val isFailed: MutableState<Boolean> = mutableStateOf(false)
)

// ForgotPasswordState State
data class ForgotPasswordState(
    var data: ForgotPasswordModel = ForgotPasswordModel(),
    var message: String = "",
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val isSucceed: MutableState<Boolean> = mutableStateOf(false),
    val isFailed: MutableState<Boolean> = mutableStateOf(false)
)
// ResetPasswordState State
data class ResetPasswordState(
    var data: ResetPasswordModel = ResetPasswordModel(),
    var message: String = "",
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val isSucceed: MutableState<Boolean> = mutableStateOf(false),
    val isFailed: MutableState<Boolean> = mutableStateOf(false)
)