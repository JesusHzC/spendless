package com.jesushz.spendless.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState

data class LoginState(
    val username: TextFieldState = TextFieldState(),
    val pin : TextFieldState = TextFieldState(),
    val canLogin: Boolean = false,
)
