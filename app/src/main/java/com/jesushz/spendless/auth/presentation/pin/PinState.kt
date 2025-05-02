package com.jesushz.spendless.auth.presentation.pin

import com.jesushz.spendless.auth.domain.PinFlow

data class PinState(
    val pin: String = "",
    val confirmPin: String = "",
    val isPinValid: Boolean = false,
    val flow: PinFlow = PinFlow.REGISTER
)
