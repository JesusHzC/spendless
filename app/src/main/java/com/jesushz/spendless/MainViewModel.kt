package com.jesushz.spendless

import androidx.lifecycle.ViewModel
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
    private val applicationScope: CoroutineScope,
    private val sessionManager: SessionManager
): ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _event = Channel<MainEvent>()
    val event = _event.receiveAsFlow()

    init {
        sessionManager.sessionExpired
            .distinctUntilChanged()
            .onEach { sessionType ->
                when (sessionType) {
                    SessionExpiredType.SESSION_EXPIRED -> {
                        applicationScope.launch {
                            dataStoreManager.clearUserData()
                            _event.send(MainEvent.OnNavigateToAuth)
                        }
                    }
                    SessionExpiredType.LOCKED_OUT -> {
                        applicationScope.launch {
                            dataStoreManager.updateLockOutEnabled(false)
                            _event.send(MainEvent.OnNavigateToPin)
                        }
                    }
                }
            }.launchIn(applicationScope)

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
                if (user != null) {
                    dataStoreManager.updateSessionMonitorEnabled(true)
                    dataStoreManager.updateLockOutEnabled(true)
                } else {
                    dataStoreManager.updateSessionMonitorEnabled(false)
                    dataStoreManager.updateLockOutEnabled(false)
                }
            }
            .launchIn(applicationScope)

        combine(
            dataStoreManager.isSessionMonitorEnabled().distinctUntilChanged(),
            dataStoreManager.getSessionDuration().distinctUntilChanged()
        ) { enabled, duration ->
            Timber.i("Session monitor enabled: $enabled, duration: $duration")
            if (enabled) {
                sessionManager.startSessionMonitor(duration.millis)
            } else {
                sessionManager.stopSessionMonitor()
            }
        }.launchIn(applicationScope)

        combine(
            dataStoreManager.isLockOutEnabled().distinctUntilChanged(),
            dataStoreManager.getLockedOutDuration().distinctUntilChanged()
        ) { enabled, duration ->
            Timber.i("Session monitor enabled: $enabled, duration: $duration")
            if (enabled) {
                sessionManager.startLockoutMonitor(duration.millis)
            } else {
                sessionManager.stopLockoutMonitor()
            }
        }.launchIn(applicationScope)
    }

    fun onUserInteraction() {
        sessionManager.touch()
    }

    fun endIsLoggedIn() {
        applicationScope.launch {
            dataStoreManager.clearUserData()
        }
    }

}