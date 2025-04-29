package com.jesushz.spendless.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState

data class RegisterState(
    val username: TextFieldState = TextFieldState(),
    val isUsernameValid: Boolean = false,
)
