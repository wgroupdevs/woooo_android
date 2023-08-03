package woooo_app.woooo.data.datasource.local

import kotlinx.coroutines.flow.Flow

interface UserPreferences {

    //    get
    fun getFirstName(): Flow<String>
    fun getLastName(): Flow<String>
    suspend fun getAuthToke(): String
    suspend fun getJID(): String
    suspend fun getProfileImage(): String
    suspend fun getDOB(): String
    suspend fun getAbout(): String
    suspend fun getAddress(): String
    suspend fun getPostalCode(): String
    suspend fun getPhone(): String
    suspend fun getEmail(): String
    suspend fun getLanguage(): String
    suspend fun getLanguageCode(): String

    //    set
    suspend fun setFirstName(firstName: String)
    suspend fun setLastName(lastName: String)
    suspend fun setEmail(email: String)
    suspend fun setPhone(phone: String)
    suspend fun setAuthToken(authToken: String)
    suspend fun setJID(jid: String)
    suspend fun setProfileImage(image: String)
    suspend fun setDOB(dob: String)
    suspend fun clear()
    suspend fun setAbout(about: String)
    suspend fun setAddress(address: String)
    suspend fun setPostalCode(postalCode: String)
    suspend fun setLanguage(language: String)
    suspend fun setLanguageCode(languageCode: String)

}