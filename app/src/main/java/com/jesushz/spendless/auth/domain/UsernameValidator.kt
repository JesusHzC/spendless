package com.jesushz.spendless.auth.domain

import java.util.regex.Pattern

class UsernameValidator {

    fun validate(username: CharSequence): Boolean {
        val pattern = Pattern.compile(
            "^[a-zA-Z0-9]{3,14}\$"
        )
        return pattern.matcher(username).matches()
    }

}