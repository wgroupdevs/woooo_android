package eu.siacs.conversations.http.model.requestmodels

data class LoginRequestParams(
    val email: String="",
    val PhoneNumber: String="",
    val password: String="",
    val deviceId: String="",
    val ipAddress: String="",
    val userAgent: String="",
)