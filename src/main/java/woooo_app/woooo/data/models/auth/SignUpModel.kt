package woooo_app.woooo.data.models.auth


data class SignUpModel(
    val success: Boolean? = null,
    val message: String? = null,
    val error: String? = null,
    val data: SignUpData? = null
)

data class SignUpData(
    val accountID: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val imageURL: String? = null,
    val id: Long? = null,
    val jID: String? = null
)