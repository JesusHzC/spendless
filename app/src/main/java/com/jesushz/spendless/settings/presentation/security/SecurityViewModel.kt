package com.jesushz.spendless.settings.presentation.security

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.preferences.SecurityPreferences
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SecurityViewModel(
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    private val _state = MutableStateFlow(SecurityState())
    val state = _state.asStateFlow()

    private val _event = Channel<SecurityEvent>()
    val event = _event.receiveAsFlow()

    init {
        viewModelScope.launch {
            val securityPreferences = dataStoreManager
                .getAllSecurityPreferences()

            _state.update {
                it.copy(
                    biometrics = securityPreferences.biometrics,
                    sessionDuration = securityPreferences.sessionDuration,
                    lockedOutDuration = securityPreferences.lockedOutDuration
                )
            }
        }
    }

    fun onAction(action: SecurityAction) {
        when (action) {
            is SecurityAction.OnBiometricsSelected -> {
                _state.update {
                    it.copy(
                        biometrics = action.biometrics
                    )
                }
            }
            is SecurityAction.OnLockedOutDurationSelected -> {
                _state.update {
                    it.copy(
                        lockedOutDuration = action.lockedOutDuration
                    )
                }
            }
            is SecurityAction.OnSessionDurationSelected -> {
                _state.update {
                    it.copy(
                        sessionDuration = action.sessionDuration
                    )
                }
            }
            SecurityAction.OnSaveClick -> onSave()
            else -> Unit
        }
    }

    private fun onSave() {
        val securityPreferences = SecurityPreferences(
            biometrics = state.value.biometrics,
            sessionDuration = state.value.sessionDuration,
            lockedOutDuration = state.value.lockedOutDuration
        )
        viewModelScope.launch {
            _event.send(SecurityEvent.OnSaveSuccess)
            dataStoreManager.saveAllSecurityPreferences(securityPreferences)
        }
    }

}