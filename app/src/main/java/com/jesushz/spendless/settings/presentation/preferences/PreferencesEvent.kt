package com.jesushz.spendless.settings.presentation.preferences

sealed interface PreferencesEvent {

    data object OnSavePreferencesSuccess: PreferencesEvent

}
