package woooo_app.woooo.utils

import android.text.TextUtils
import android.util.Patterns

object Validators {

//    fun isValidEmail(target: CharSequence?): Boolean {
//        return if (TextUtils.isEmpty(target)) {
//            false
//        } else {
//            Patterns.EMAIL_ADDRESS.matcher(target).matches()
//        }
//    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun isStringContainNumeric(target: CharSequence): Boolean {
        val regex = Regex("[0-9]")
        return !regex.containsMatchIn(target)
    }
}