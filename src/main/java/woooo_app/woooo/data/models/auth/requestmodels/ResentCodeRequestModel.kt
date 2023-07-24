package woooo_app.woooo.data.models.auth.requestmodels

data class ReSentOTPRequestModel(
    val email: String
)

data class BaseResendCodeReqParam(
    val email:ReSentOTPRequestModel,
    val IsOtpForAccount: Boolean = false
)