package com.jesushz.spendless.auth.presentation.register

import com.jesushz.spendless.core.presentation.ui.UiText

sealed interface RegisterEvent {

    data class OnError(val error: UiText): RegisterEvent
    data class OnUsernameSuccess(val username: String): RegisterEvent

}
