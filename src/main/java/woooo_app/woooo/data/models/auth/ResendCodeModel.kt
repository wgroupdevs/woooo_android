package woooo_app.woooo.data.models.auth

data class ResendCodeModel(
    val succeeded: Boolean? = null,
    val message: Any? = null,
    val errors: Any? = null,
    val data: ResendCodeData? = null
)

data class ResendCodeData(
    val id: Any? = null,
    val otp: String? = null,
    val efCode: String? = null,
    val isUse: Boolean? = null,
    val createdBy: String? = null,
    val created: String? = null,
    val lastModifiedBy: String? = null,
    val lastModified: String? = null
)
