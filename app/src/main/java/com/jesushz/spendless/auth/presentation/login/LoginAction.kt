package com.jesushz.spendless.auth.presentation.login

import androidx.compose.ui.text.input.TextFieldValue
import com.jesushz.spendless.core.presentation.ui.UiText

sealed interface LoginAction {

    data object OnLoginClick : LoginAction
    data object OnRegisterClick : LoginAction
    data class OnUsernameChange(val username: TextFieldValue) : LoginAction
    data class OnPinChange(val pin: TextFieldValue) : LoginAction
    data object OnBiometricsSuccess : LoginAction
    data class OnBiometricsError(val error: UiText, val confirmLogout: Boolean) : LoginAction

}
