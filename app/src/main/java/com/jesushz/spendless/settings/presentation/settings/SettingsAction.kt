package com.jesushz.spendless.settings.presentation.settings

sealed interface SettingsAction {

    data object OnPreferencesClick: SettingsAction
    data object OnSecurityClick: SettingsAction
    data object OnLogOutClick: SettingsAction
    data object OnBackClick: SettingsAction

}
