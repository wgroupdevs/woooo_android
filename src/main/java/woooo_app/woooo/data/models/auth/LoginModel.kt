package woooo_app.woooo.data.models.auth


data class LoginModel (
    val success: Boolean? = null,
    val message: String? = null,
    val error: Any? = null,
    val data: LoginModelData? = null
)

data class LoginModelData (
    val token: String? = null,
    val user: User? = null
)

data class User (
    val accountID: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val imageURL: String? = null,
    val id: Long? = null,
    val jID: String? = null
)
