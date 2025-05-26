package com.jesushz.spendless

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.security.SessionExpiredType
import com.jesushz.spendless.core.domain.security.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val dataStoreManager: DataStoreManager,
    private val applicationScope: CoroutineScope
): ViewModel() {

    private var sessionManager: SessionManager? = null

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _event = Channel<MainEvent>()
    val event = _event.receiveAsFlow()

    init {
        sessionManager = SessionManager(
            dataStoreManager = dataStoreManager,
            applicationScope = applicationScope,
            onSessionExpired = { type ->
                when (type) {
                    SessionExpiredType.SESSION_EXPIRED -> {
                        viewModelScope.launch {
                            _event.send(MainEvent.OnNavigateToAuth)
                            dataStoreManager.updateSessionMonitorEnabled(false)
                            dataStoreManager.clearUserData()
                        }
                    }
                    SessionExpiredType.LOCKED_OUT -> {
                        viewModelScope.launch {
                            _event.send(MainEvent.OnNavigateToPin)
                            dataStoreManager.updateLockOutEnabled(false)
                        }
                    }
                }
            }
        )

        dataStoreManager
            .getUser()
            .distinctUntilChanged()
            .onEach { user ->
                _state.update {
                    it.copy(
                        user = user,
                        isLoggedIn = user != null
                    )
                }
                if (user != null) {
                    viewModelScope.launch {
                        dataStoreManager.updateSessionMonitorEnabled(true)
                        dataStoreManager.updateLockOutEnabled(true)
                    }
                } else {
                    viewModelScope.launch {
                        dataStoreManager.updateSessionMonitorEnabled(false)
                        dataStoreManager.updateLockOutEnabled(false)
                    }
                }
            }
            .launchIn(viewModelScope)

        dataStoreManager
            .isSessionMonitorEnabled()
            .distinctUntilChanged()
            .onEach { enabled ->
                Timber.i("Session monitor enabled: $enabled")
                if (enabled) {
                    sessionManager?.startSessionMonitor()
                } else {
                    sessionManager?.stopSessionMonitor()
                }
            }
            .launchIn(viewModelScope)

        dataStoreManager
            .isLockOutEnabled()
            .distinctUntilChanged()
            .onEach { enabled ->
                Timber.i("Lockout enabled: $enabled")
                if (enabled) {
                    sessionManager?.startLockoutMonitor()
                } else {
                    sessionManager?.stopLockoutMonitor()
                }
            }
            .launchIn(viewModelScope)
    }

    fun onUserInteraction() {
        sessionManager?.touch()
    }

}