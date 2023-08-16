package woooo_app.woooo.data.datasource.local

import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKey {
    val AUTH_TOKEN = stringPreferencesKey("AUTH_TOKEN")
    val ACCOUNT_JID = stringPreferencesKey("ACCOUNT_JID")
    val FIRST_NAME = stringPreferencesKey("FIRST_NAME")
    val LAST_NAME = stringPreferencesKey("LAST_NAME")
    val PHONE_NUMBER = stringPreferencesKey("PHONE_NUMBER")
    val EMAIL = stringPreferencesKey("EMAIL")
    val PROFILE_IMAGE = stringPreferencesKey("PROFILE_IMAGE")
    val DATE_OF_BIRTH = stringPreferencesKey("DATE_OF_BIRTH")
    val ABOUT = stringPreferencesKey("ABOUT")
    val ADDRESS = stringPreferencesKey("ADDRESS")
    val POSTAL_CODE = stringPreferencesKey("POSTAL_CODE")
    val LANGUAGE = stringPreferencesKey("LANGUAGE")
    val LANGUAGE_CODE = stringPreferencesKey("LANGUAGE_CODE")
    val ACCOUNT_UNIQUE_ID = stringPreferencesKey("ACCOUNT_UNIQUE_ID")
    val SOCKET_ID = stringPreferencesKey("SOCKET_ID")

}