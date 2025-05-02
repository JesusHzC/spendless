package com.jesushz.spendless.auth.presentation.pin

import com.jesushz.spendless.core.presentation.ui.UiText

sealed interface PinEvent {

    data class OnError(val message: UiText): PinEvent

}