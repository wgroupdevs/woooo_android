package woooo_app.woooo.data.models.settings.requestModels

data class ChangePasswordReqModel(
    val accountId: String,val currentPassword: String,val newPassword: String
)