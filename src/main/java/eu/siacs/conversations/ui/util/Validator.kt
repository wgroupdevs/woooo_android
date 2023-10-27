package eu.siacs.conversations.ui.util

import java.util.regex.Matcher
import java.util.regex.Pattern


fun isValidEmail(email: String): Boolean {
    val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    val inputStr: CharSequence = email
    val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher: Matcher = pattern.matcher(inputStr)
    return matcher.matches()
}