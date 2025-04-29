package com.jesushz.spendless.auth.presentation.register

sealed interface RegisterAction {

    data object OnNextButtonClick: RegisterAction
    data object OnLoginClick: RegisterAction

}
