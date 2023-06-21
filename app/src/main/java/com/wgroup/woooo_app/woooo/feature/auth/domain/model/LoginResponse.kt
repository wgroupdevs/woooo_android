package com.wgroup.woooo_app.woooo.feature.auth.domain.model

data class LoginResponse (
    val succeeded: Boolean=false,
    val message: Any? = null,
    val errors: Any? = null,
    val data: Data?=null
)

data class Data (
    val id: String?,
    val userName: String?,
    val email: String?,
    val phoneNumber: String?,
    val firstName: String?,
    val lastName: Any? = null,
    val password: Any? = null,
    val roles: List<String>?,
    val isVerified: Boolean=false,
    val jwToken: String?,
    val ipAddress: Any? = null,
    val isWalletPinSet: Boolean=false,
    val isProfileSetup: Boolean=false
)
