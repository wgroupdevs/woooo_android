package eu.siacs.conversations.http.model.requestmodels

data class ConfirmAccountRequestModel(
    val email: String,val code: String

) {
    fun toMap(): Map<String,String> {
        return mapOf(
            "email" to email,
            "code" to code,
        )
    }
}