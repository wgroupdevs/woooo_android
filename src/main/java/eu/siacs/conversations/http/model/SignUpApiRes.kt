package eu.siacs.conversations.http.model

data class SignUpModel(
    var Success: Boolean? = null,
    val Message: String? = null,
    val Error: String? = null,
    val Data: SignUpData? = null
)

data class SignUpData(
    val accountID: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val imageURL: String? = null,
    val id: Long? = null,
    val jID: String? = null,
    val walletAddress: String? = null
)

data class SignUpRequestModel(
    val deviceId: String = "",
    val email: String = "",
    val firstName: String = "",
    val ipAddress: String = "",
    val lastName: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val userReferralCode: String = "",
    val walletAddress: String = ""
)