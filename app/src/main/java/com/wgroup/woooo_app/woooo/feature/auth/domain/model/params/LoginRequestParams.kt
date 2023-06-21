package com.wgroup.woooo_app.woooo.feature.auth.domain.model.params

data class LoginRequestParams(
    val email: String="",
    val deviceName: String="",
    val password: String=""
)