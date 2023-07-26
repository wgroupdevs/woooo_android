package woooo_app.woooo.data.datasource.local

import kotlinx.coroutines.flow.Flow

interface UserPreferences {

    //    get
    fun getFirstName(): Flow<String>
    fun getLastName(): Flow<String>
    suspend fun getAuthToke(): String
    suspend fun getProfileImage(): String

    //    set
    suspend fun setFirstName(firstName: String)
    suspend fun setLastName(lastName: String)
    suspend fun setEmail(email: String)
    suspend fun setPhone(phone: String)
    suspend fun setAuthToken(authToken: String)
    suspend fun setProfileImage(image: String)

}