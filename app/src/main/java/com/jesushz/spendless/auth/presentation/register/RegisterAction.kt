package com.jesushz.spendless.auth.presentation.register

import androidx.compose.ui.text.input.TextFieldValue

sealed interface RegisterAction {

    data object OnNextButtonClick: RegisterAction
    data object OnLoginClick: RegisterAction
    data class OnUsernameChange(val username: TextFieldValue): RegisterAction

}
