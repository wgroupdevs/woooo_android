package com.wgroup.woooo_app.woooo.utils

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
}