package com.jesushz.spendless

import com.jesushz.spendless.core.domain.user.User

data class MainState(
    val isLoading: Boolean = true,
    val isLoggedIn: Boolean = false,
    val user: User? = null
)
