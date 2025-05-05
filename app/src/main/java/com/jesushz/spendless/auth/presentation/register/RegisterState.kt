package com.jesushz.spendless.auth.presentation.register

import androidx.compose.ui.text.input.TextFieldValue

data class RegisterState(
    val username: TextFieldValue = TextFieldValue(),
    val isUsernameValid: Boolean = false,
)
