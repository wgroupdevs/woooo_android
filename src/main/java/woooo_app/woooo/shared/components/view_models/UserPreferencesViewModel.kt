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

    suspend fun setSocketId(socket: String) {
        userPreferences.setSocketId(socket)
    }

    suspend fun getSocketId(): String {
        return userPreferences.getSocketId()
    }

    suspend fun setAbout(about: String) {
        userPreferences.setAbout(about)
    }

    suspend fun getAbout(): String {
        return userPreferences.getAbout()
    }

    suspend fun setDOB(dob: String) {
        userPreferences.setDOB(dob)
    }

    suspend fun getDOB(): String {
        return userPreferences.getDOB()
    }

    suspend fun setAddress(address: String) {
        userPreferences.setAddress(address)
    }

    suspend fun getAddress(): String {
        return userPreferences.getAddress()
    }

    suspend fun setPostalCode(postalCode: String) {
        userPreferences.setPostalCode(postalCode)
    }

    suspend fun getPostalCode(): String {
        return userPreferences.getPostalCode()
    }

    suspend fun setLastName(lastName: String) {
        userPreferences.setLastName(lastName)
    }

    suspend fun setEmail(email: String) {
        userPreferences.setEmail(email)
    }

    suspend fun getLanguage(): String {
        return userPreferences.getLanguage()
    }

    suspend fun setLanguage(language: String) {
        userPreferences.setLanguage(language)
    }

    suspend fun getLanguageCode(): String {
        return userPreferences.getLanguageCode()
    }

    suspend fun setLanguageCode(languageCode: String) {
        userPreferences.setLanguageCode(languageCode)
    }

    suspend fun getEmail(): String {
        return userPreferences.getEmail()
    }

    suspend fun setPhone(phone: String) {
        userPreferences.setPhone(phone)
    }

    suspend fun getPhone(): String {
        return userPreferences.getPhone()
    }

    suspend fun setAuthToken(authToken: String) {
        userPreferences.setAuthToken(authToken)
    }

    suspend fun setJID(jid: String) {
        userPreferences.setJID(jid)
    }

    suspend fun getProfileImage(): String {
        return userPreferences.getProfileImage()
    }

    suspend fun setProfileImage(image: String) {
        userPreferences.setProfileImage(image)
    }

    suspend fun setAccountUniqueId(uniqueId: String) {
        userPreferences.setAccountUniqueId(uniqueId)
    }

    suspend fun getAccountUniqueId(): String {
        return userPreferences.getAccountUniqueId()
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
        userPreferences.clear()
    }


}