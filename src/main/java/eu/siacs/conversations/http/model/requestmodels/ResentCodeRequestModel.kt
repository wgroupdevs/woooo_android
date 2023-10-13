package eu.siacs.conversations.http.model.requestmodels

data class EmailRequestModel(
    val email: String
)

data class BaseResendCodeReqParam(
    val email: EmailRequestModel,
    val IsOtpForAccount: Boolean = false
)