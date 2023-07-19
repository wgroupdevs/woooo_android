package woooo_app.woooo.data.models

import woooo_app.woooo.data.models.auth.SignUpData

class BaseModel(
    val Success: Boolean? = null,
    val Message: String? = null,
    val Error: Any? = null,
    val Data: Any? = null
)