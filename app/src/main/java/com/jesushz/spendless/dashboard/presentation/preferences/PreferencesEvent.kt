package com.jesushz.spendless.dashboard.presentation.preferences

sealed interface PreferencesEvent {

    data object OnSavePreferencesSuccess: PreferencesEvent

}
