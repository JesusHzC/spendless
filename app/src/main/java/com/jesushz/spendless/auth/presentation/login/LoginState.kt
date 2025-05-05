package com.jesushz.spendless.auth.presentation.login

import androidx.compose.ui.text.input.TextFieldValue

data class LoginState(
    val username: TextFieldValue = TextFieldValue(),
    val pin : TextFieldValue = TextFieldValue(),
    val canLogin: Boolean = false,
)
