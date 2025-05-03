package com.jesushz.spendless.auth.presentation.login

import com.jesushz.spendless.core.presentation.ui.UiText

sealed interface LoginEvent {

    data class OnError(val error: UiText): LoginEvent
    data object OnLoginSuccess: LoginEvent

}
