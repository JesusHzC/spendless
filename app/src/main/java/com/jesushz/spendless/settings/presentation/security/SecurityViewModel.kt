package com.jesushz.spendless.settings.presentation.security

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SecurityViewModel(
    private val dataStoreManager: DataStoreManager,
    private val applicationScope: CoroutineScope
): ViewModel() {

    private val _state = MutableStateFlow(SecurityState())
    val state = _state.asStateFlow()

    private val _event = Channel<SecurityEvent>()
    val event = _event.receiveAsFlow()

    init {
        dataStoreManager
            .getBiometrics()
            .onEach { biometrics ->
                _state.update {
                    it.copy(
                        biometrics = biometrics
                    )
                }
            }
            .launchIn(viewModelScope)

        dataStoreManager
            .getSessionDuration()
            .onEach { sessionDuration ->
                _state.update {
                    it.copy(
                        sessionDuration = sessionDuration
                    )
                }
            }
            .launchIn(viewModelScope)

        dataStoreManager
            .getLockedOutDuration()
            .onEach { duration ->
                _state.update {
                    it.copy(
                        lockedOutDuration = duration
                    )
                }
            }
            .launchIn(viewModelScope)
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

    private suspend fun savePreferences() {
        applicationScope.launch {
            dataStoreManager.saveBiometrics(state.value.biometrics)
            dataStoreManager.saveSessionDuration(state.value.sessionDuration)
            dataStoreManager.saveLockedOutDuration(state.value.lockedOutDuration)
        }.join()
    }

    private fun onSave() {
        viewModelScope.launch {
            savePreferences()
            _event.send(SecurityEvent.OnSaveSuccess)
        }
    }

}