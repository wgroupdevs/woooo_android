package woooo_app.woooo.data.models.auth

data class SignUpRequestModel(
    val deviceId: String = "",
    val email: String = "",
    val firstName: String = "",
    val ipAddress: String = "",
    val lastName: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val userReferralCode: String = ""
)