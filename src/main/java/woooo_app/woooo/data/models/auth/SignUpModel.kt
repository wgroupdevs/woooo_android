package woooo_app.woooo.data.models.auth


data class SignUpModel (
    val success: Boolean? = null,
    val message: String? = null,
    val error: Any? = null,
    val data: SignUpData? = null
)

data class SignUpData (
    val id: Long? = null,
    val accountID: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val imageURL: String? = null,
    val userName: String? = null
)