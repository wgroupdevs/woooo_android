package woooo_app.woooo.shared.components.view_models

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import woooo_app.woooo.data.datasource.local.UserPreferences
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {


    suspend fun setFirstName(firstName: String) {
        userPreferences.setFirstName(firstName)
    }

    suspend fun setLastName(lastName: String) {
        userPreferences.setLastName(lastName)
    }

    suspend fun setEmail(email: String) {
        userPreferences.setEmail(email)
    }

    suspend fun setPhone(phone: String) {
        userPreferences.setPhone(phone)
    }

    suspend fun setAuthToken(authToken: String) {
        userPreferences.setAuthToken(authToken)
    }


    suspend fun setJID(jid: String) {
        userPreferences.setJID(jid)
    }

    suspend fun setProfileImage(image: String) {
        userPreferences.setProfileImage(image)
    }

    suspend fun getFirstName(): String {
        return userPreferences.getFirstName().first()
    }

    suspend fun getLastName(): String {
        return userPreferences.getLastName().first()
    }

    suspend fun getAuthToke(): String {
        return userPreferences.getAuthToke()
    }

    suspend fun getJID(): String {
        return userPreferences.getJID()
    }

    suspend fun clearPreference() {
        userPreferences.clear();
    }


}