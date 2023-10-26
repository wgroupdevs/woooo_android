package eu.siacs.conversations.persistance

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
 class WOOPrefManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("WOOAppPreferences", Context.MODE_PRIVATE)

    // Define methods to save and retrieve data

    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }


    companion object {

        const val USER_TOKEN_KEY = "user_token_key"
        const val WALLET_ADDRESS_KEY = "wallet_address_key"


    }


    // You can add similar methods for other data types (e.g., Boolean, Float, Long, etc.)
}
