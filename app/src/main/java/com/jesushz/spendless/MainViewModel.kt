package com.jesushz.spendless

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.security.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
        dataStoreManager
            .getUser()
            .onEach { user ->
                _state.update {
                    it.copy(
                        user = user,
                        isLoggedIn = user != null
                    )
                }
            }
            .launchIn(viewModelScope)

        sessionManager = SessionManager(
            dataStoreManager = dataStoreManager,
            applicationScope = applicationScope,
            onSessionExpired = {
                applicationScope.launch {
                    updateIsSessionManagerPaused(true)
                    _event.send(MainEvent.OnNavigateToAuth)
                }
            },
            onLockOut = {
                applicationScope.launch {
                    updateIsSessionManagerPaused(true)
                    _event.send(MainEvent.OnNavigateToPin)
                }
            }
        )
    }

    fun onStartSession() {
        val isSessionManagerPaused = state.value.isSessionManagerPaused
        val isLoggedIn = state.value.isLoggedIn

        if (!isLoggedIn || isSessionManagerPaused) {
            sessionManager?.stop()
            return
        }

        sessionManager?.start()
    }

    fun onStopSession() {
        sessionManager?.stop()
    }

    fun onTouch() {
        sessionManager?.touch()
    }

    fun updateIsSessionManagerPaused(isPaused: Boolean) {
        _state.update {
            it.copy(isSessionManagerPaused = isPaused)
        }
    }

}