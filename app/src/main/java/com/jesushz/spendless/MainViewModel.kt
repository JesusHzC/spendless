package com.jesushz.spendless

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.auth.domain.PinFlow
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.security.SessionManager
import com.jesushz.spendless.core.util.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        viewModelScope.launch {
            val user = dataStoreManager.getUser()
            _state.update {
                it.copy(
                    user = user,
                    isLoggedIn = user != null,
                    isLoading = false
                )
            }
        }

        sessionManager = SessionManager(
            dataStoreManager = dataStoreManager,
            applicationScope = applicationScope,
            onSessionExpired = {
                updateIsSessionManagerPaused(true)
                applicationScope.launch {
                    _event.send(MainEvent.OnNavigateToAuth)
                }
            },
            onLockOut = {
                updateIsSessionManagerPaused(true)
                applicationScope.launch {
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

        if (!state.value.isSessionManagerPaused) {
            sessionManager?.start()
        }
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