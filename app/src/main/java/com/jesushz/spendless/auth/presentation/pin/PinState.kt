package com.jesushz.spendless.auth.presentation.pin

data class PinState(
    val pin: String = "",
    val isPinValid: Boolean = false,
)
