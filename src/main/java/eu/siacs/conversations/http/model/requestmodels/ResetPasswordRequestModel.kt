package eu.siacs.conversations.http.model.requestmodels


data class ResetPasswordRequestModel(
    val confirmPassword: String,
    val email: String,
    val otp: String,
    val password: String
)