package com.wgroup.woooo_app.woooo.feature.auth.viewmodel

import com.wgroup.woooo_app.woooo.data.models.LoginModel
import com.wgroup.woooo_app.woooo.data.models.SignUpModel

// login State
data class LoginState(
    val data: LoginModel = LoginModel(),
    val error: String = "",
    val isLoading: Boolean = false
)
// SignUp State

data class SignUpSate(
    val data: SignUpModel = SignUpModel(),
    val error: String = "",
    var isLoading: Boolean = false
)