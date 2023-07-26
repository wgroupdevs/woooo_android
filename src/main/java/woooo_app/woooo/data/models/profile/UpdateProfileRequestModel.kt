package woooo_app.woooo.data.models.profile

data class UpdateProfileRequestModel(
    val address1: String,
    val city: String,
    val country: String,
    val dateOfBirth: String,
    val description: String,
    val firstName: String,
    val imageURL: String,
    val language: String,
    val languageCode: String,
    val lastName: String,
    val state: String,
    val zipCode: String
) {
    fun toMap(): Map<String,String> {
        return mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "imageURL" to imageURL,
            "dateOfBirth" to dateOfBirth,
            "language" to language,
            "description" to description,
            "address1" to address1,
            "city" to city,
            "country" to country,
            "zipCode" to zipCode,
            "state" to state,
            "languageCode" to languageCode,
        )
    }
}