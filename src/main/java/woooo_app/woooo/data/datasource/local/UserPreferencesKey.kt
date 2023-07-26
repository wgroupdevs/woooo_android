package woooo_app.woooo.data.datasource.local

import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKey {
    val AUTH_TOKEN = stringPreferencesKey("AUTH_TOKEN")
    val FIRST_NAME = stringPreferencesKey("FIRST_NAME")
    val LAST_NAME = stringPreferencesKey("LAST_NAME")
    val PHONE_NUMBER = stringPreferencesKey("PHONE_NUMBER")
    val EMAIL = stringPreferencesKey("EMAIL")
    val PROFILE_IMAGE = stringPreferencesKey("PROFILE_IMAGE")
}