package woooo_app.woooo.data.models

import woooo_app.woooo.data.models.auth.SignUpData

class BaseModel(
    val success: Boolean? = null,
    val message: String? = null,
    val error: Any? = null,
    val data: Any? = null
)