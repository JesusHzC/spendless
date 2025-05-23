package com.jesushz.spendless.settings.presentation.security

sealed interface SecurityEvent {

    data object OnSaveSuccess: SecurityEvent

}
