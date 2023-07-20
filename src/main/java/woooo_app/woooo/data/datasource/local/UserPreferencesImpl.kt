package woooo_app.woooo.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferencesImpl(private val dataStore: DataStore<Preferences>) : UserPreferences {


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

}