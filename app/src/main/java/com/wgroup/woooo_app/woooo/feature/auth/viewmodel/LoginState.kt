package com.wgroup.woooo_app.woooo.feature.auth.viewmodel

import com.wgroup.woooo_app.woooo.feature.auth.domain.model.LoginResponse
import com.wgroup.woooo_app.woooo.feature.auth.domain.model.params.LoginRequestParams

data class LoginState(
    val data: LoginResponse =LoginResponse(),
    val error: String = "",
    val isLoading: Boolean = false
)

