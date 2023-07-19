package woooo_app.woooo.data.models.auth.requestmodels

data class LoginRequestParams(
    val email: String="",
    val PhoneNumber: String="",
    val password: String="",
    val deviceId: String="",
    val ipAddress: String="",
    val userAgent: String="",
)