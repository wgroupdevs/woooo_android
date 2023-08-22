package woooo_app.woooo.shared.components.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.siacs.conversations.http.model.UserBasicInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import woooo_app.woooo.data.datasource.local.UserPreferences
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    var user: UserBasicInfo = UserBasicInfo()

    val TAG="UserPrefViewModel"
    init {

        Log.d(TAG,"UserPreferencesViewModel_init called")
        viewModelScope.launch {
            user.setFirstName(getFirstName())
            user.setLanguage(getLanguage())
            user.setPhoneNumber(getPhone())
            user.setLastName(getLastName())
            user.setAccountId(getAccountUniqueId())
            user.setEmail(getEmail())
            user.setJid(getJID())
        }
    }

    fun getUserInfo() = user


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