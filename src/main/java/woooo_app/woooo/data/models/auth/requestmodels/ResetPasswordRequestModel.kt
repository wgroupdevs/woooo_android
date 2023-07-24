package woooo_app.woooo.data.models.auth.requestmodels


data class ResetPasswordRequestModel(
    val confirmPassword: String,
    val email: String,
    val otp: String,
    val password: String
)