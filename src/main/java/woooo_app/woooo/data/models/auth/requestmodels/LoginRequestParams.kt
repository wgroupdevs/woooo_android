package woooo_app.woooo.data.models.auth.requestmodels

data class LoginRequestParams(
    val email: String="",
    val deviceName: String="",
    val password: String=""
)