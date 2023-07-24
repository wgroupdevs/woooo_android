package woooo_app.woooo.feature.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

object EmailForAuthModule {
    private val setEmail = mutableStateOf("")
    val getEmail: State<String> = setEmail
    fun setEmailValue(value: String) {
        setEmail.value = value
    }
}