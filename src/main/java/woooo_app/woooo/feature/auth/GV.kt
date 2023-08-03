package woooo_app.woooo.feature.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

object GV {
    private val setEmail = mutableStateOf("")
    val getEmail: State<String> = setEmail
    fun setEmailValue(value: String) {
        setEmail.value = value
    }

    fun clearEmailField() {
        setEmailValue("")
    }

    val getUserProfileImage = mutableStateOf("")
    val getFirstName = mutableStateOf("")
}