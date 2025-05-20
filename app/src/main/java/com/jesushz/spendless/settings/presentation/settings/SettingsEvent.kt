package com.jesushz.spendless.settings.presentation.settings

sealed interface SettingsEvent {

    data object OnLogOutSuccess: SettingsEvent

}
