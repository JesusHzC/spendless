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
import kotlinx.coroutines.flow.combine
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
            applicationScope = applicationScope,
            onSessionExpired = { type ->
                when (type) {
                    SessionExpiredType.SESSION_EXPIRED -> {
                        viewModelScope.launch {
                            dataStoreManager.setIsLoggedIn(false)
                            _event.send(MainEvent.OnNavigateToAuth)
                        }
                    }
                    SessionExpiredType.LOCKED_OUT -> {
                        viewModelScope.launch {
                            dataStoreManager.updateLockOutEnabled(false)
                            _event.send(MainEvent.OnNavigateToPin)
                        }
                    }
                }
            }
        )

        dataStoreManager
            .getUser()
            .distinctUntilChanged()
            .onEach { user ->
                Timber.i("User data updated: $user")
                _state.update {
                    it.copy(
                        user = user
                    )
                }
            }
            .launchIn(viewModelScope)

        dataStoreManager
            .isLoggedIn()
            .distinctUntilChanged()
            .onEach { isLoggedIn ->
                _state.update {
                    it.copy(
                        isLoggedIn = isLoggedIn
                    )
                }
                if (isLoggedIn) {
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

        combine(
            dataStoreManager.isSessionMonitorEnabled().distinctUntilChanged(),
            dataStoreManager.getSessionDuration().distinctUntilChanged()
        ) { enabled, duration ->
            Timber.i("Session monitor enabled: $enabled, duration: $duration")
            if (enabled) {
                sessionManager?.startSessionMonitor(duration.millis)
            } else {
                sessionManager?.stopSessionMonitor()
            }
        }.launchIn(viewModelScope)

        combine(
            dataStoreManager.isLockOutEnabled().distinctUntilChanged(),
            dataStoreManager.getLockedOutDuration().distinctUntilChanged()
        ) { enabled, duration ->
            Timber.i("Session monitor enabled: $enabled, duration: $duration")
            if (enabled) {
                sessionManager?.startLockoutMonitor(duration.millis)
            } else {
                sessionManager?.stopLockoutMonitor()
            }
        }.launchIn(viewModelScope)
    }

    fun onUserInteraction() {
        sessionManager?.touch()
    }

    fun endIsLoggedIn() {
        viewModelScope.launch {
            dataStoreManager.setIsLoggedIn(false)
        }
    }

}