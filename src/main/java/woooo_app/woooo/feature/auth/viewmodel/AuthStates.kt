package com.wgroup.woooo_app.woooo.feature.auth.viewmodel

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
    val data: SignUpModel = SignUpModel(),
    val error: String = "",
    var isLoading: Boolean = false
)