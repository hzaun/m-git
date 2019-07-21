package com.nuzharukiya.gitm.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Nuzha Rukiya on 19/07/21.
 */

class ValidationUtils {
    private val GIT_USERNAME_PATTERN = "([a-z0-9]+)(([a-z0-9]*)(-)*([a-z0-9]*))*([a-z0-9]+)"

    private var pattern: Pattern = Pattern.compile(GIT_USERNAME_PATTERN)
    private var matcher: Matcher? = null

    companion object {
        var INSTANCE: ValidationUtils? = null

        fun getInstance(): ValidationUtils {
            if (INSTANCE == null) {
                INSTANCE = ValidationUtils()
            }

            return INSTANCE!!
        }
    }

    /**
     * Validate username with regular expression
     * @param username username for validation
     * @return true valid username, false invalid username
     */
    fun validateUsername(username: String): Boolean {
        matcher = pattern.matcher(username)
        return matcher!!.matches()
    }
}