package woooo_app.woooo.data.datasource.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferencesImpl(private val dataStore: DataStore<Preferences>) : UserPreferences {

    val TAG = "UserPreferences"

    override suspend fun setFirstName(firstName: String) {
        dataStore.edit {
            it[UserPreferencesKey.FIRST_NAME] = firstName
        }
    }

    override suspend fun setLastName(lastName: String) {
        dataStore.edit {
            it[UserPreferencesKey.LAST_NAME] = lastName
        }
    }

    override suspend fun setEmail(email: String) {
        dataStore.edit {
            it[UserPreferencesKey.EMAIL] = email
        }
    }

    override suspend fun setPhone(phone: String) {
        dataStore.edit {
            it[UserPreferencesKey.PHONE_NUMBER] = phone
        }
    }

    override suspend fun setAuthToken(authToken: String) {
        dataStore.edit {
            it[UserPreferencesKey.AUTH_TOKEN] = authToken
        }
    }

    override suspend fun setJID(jid: String) {
        dataStore.edit {
            it[UserPreferencesKey.ACCOUNT_JID] = jid
        }
    }

    override suspend fun setProfileImage(image: String) {
        dataStore.edit { it[UserPreferencesKey.PROFILE_IMAGE] = image }
    }

    override suspend fun setDOB(dob: String) {
        dataStore.edit { it[UserPreferencesKey.DATE_OF_BIRTH] = dob }

    }

    override fun getFirstName(): Flow<String> {
        return dataStore.data.catch { emit(emptyPreferences()) }.map {
            it[UserPreferencesKey.FIRST_NAME] ?: ""
        }
    }

    override fun getLastName(): Flow<String> {
        return dataStore.data.catch { emit(emptyPreferences()) }.map {
            it[UserPreferencesKey.LAST_NAME] ?: ""
        }
    }

    override suspend fun getAuthToke(): String {
        return dataStore.data.first()[UserPreferencesKey.AUTH_TOKEN] ?: ""
    }

    override suspend fun getJID(): String {
        return dataStore.data.first()[UserPreferencesKey.ACCOUNT_JID] ?: ""
    }

    override suspend fun getProfileImage(): String {
        return dataStore.data.first()[UserPreferencesKey.PROFILE_IMAGE] ?: ""
    }

    override suspend fun getDOB(): String {
        return dataStore.data.first()[UserPreferencesKey.DATE_OF_BIRTH] ?: ""

    }

    override suspend fun getAbout(): String {
        return dataStore.data.first()[UserPreferencesKey.ABOUT] ?: ""
    }

    override suspend fun getAddress(): String {
        return dataStore.data.first()[UserPreferencesKey.ADDRESS] ?: ""
    }

    override suspend fun getPostalCode(): String {
        return dataStore.data.first()[UserPreferencesKey.POSTAL_CODE] ?: ""
    }

    override suspend fun getPhone(): String {
        return dataStore.data.first()[UserPreferencesKey.PHONE_NUMBER] ?: ""
    }

    override suspend fun getEmail(): String {
        return dataStore.data.first()[UserPreferencesKey.EMAIL] ?: ""
    }

    override suspend fun getLanguage(): String {
        return dataStore.data.first()[UserPreferencesKey.LANGUAGE] ?: ""
    }

    override suspend fun getLanguageCode(): String {
        return dataStore.data.first()[UserPreferencesKey.LANGUAGE_CODE] ?: ""
    }

    override suspend fun getAccountUniqueId(): String {
        return dataStore.data.first()[UserPreferencesKey.ACCOUNT_UNIQUE_ID] ?: ""
    }

    override suspend fun clear() {
        dataStore.edit {
            Log.d(TAG,"Clearing UserPreference")
            it.clear()
        }
    }

    override suspend fun setAbout(about: String) {
        dataStore.edit { it[UserPreferencesKey.ABOUT] = about }

    }

    override suspend fun setAddress(address: String) {
        dataStore.edit { it[UserPreferencesKey.ADDRESS] = address }
    }

    override suspend fun setPostalCode(postalCode: String) {
        dataStore.edit { it[UserPreferencesKey.POSTAL_CODE] = postalCode }
    }

    override suspend fun setLanguage(language: String) {
        dataStore.edit { it[UserPreferencesKey.LANGUAGE] = language }
    }

    override suspend fun setLanguageCode(languageCode: String) {
        dataStore.edit { it[UserPreferencesKey.LANGUAGE_CODE] = languageCode }
    }

    override suspend fun setAccountUniqueId(uniqueId: String) {
        dataStore.edit { it[UserPreferencesKey.ACCOUNT_UNIQUE_ID] = uniqueId }

    }


}