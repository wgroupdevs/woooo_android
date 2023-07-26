package woooo_app.woooo.data.models.profile
data class UpdateProfileModel (
    val Success: Boolean? = null,
    val Message: String? = null,
    val Error: String? = null,
    val Data: UpdateProfileModelData? = null
)

data class UpdateProfileModelData (
    val accountID: String? = null,
    val imageURL: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val jid: String? = null,
    val id: Long? = null,
    val description: String? = null,
    val address1: String? = null,
    val address2: String? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val zipCode: String? = null,
    val language: String? = null,
    val languageCode: String? = null,
    val dateOfBirth: String? = null
)