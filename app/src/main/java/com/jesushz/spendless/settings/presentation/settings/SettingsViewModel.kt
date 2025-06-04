package com.jesushz.spendless.settings.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    private val _event = Channel<SettingsEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.OnLogOutClick -> {
                logout()
            }
            else -> Unit
        }
    }

    private fun logout() {
        viewModelScope.launch {
            dataStoreManager.clearUserData()
            _event.send(SettingsEvent.OnLogOutSuccess)
        }
    }

}