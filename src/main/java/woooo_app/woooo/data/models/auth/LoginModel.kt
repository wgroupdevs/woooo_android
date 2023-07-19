package woooo_app.woooo.data.models.auth

data class LoginModel (
    val succeeded: Boolean=false,
    val message: Any? = null,
    val errors: Any? = null,
    val loginData: LoginData?=null
)

data class LoginData (
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
