package com.jesushz.spendless.auth.presentation.login

import androidx.compose.runtime.snapshotFlow
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.auth.domain.UsernameValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update

class LoginViewModel(
    private val validator: UsernameValidator
): ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val username = snapshotFlow {
        state.value.username.text
    }
    private val pin = snapshotFlow {
        state.value.pin.text
    }

    init {
        combine(username, pin) { username, pin ->
            val isValidUsername = validator.validate(username)
            val isValidPin = pin.length == 5 && pin.isDigitsOnly()

            _state.update {
                it.copy(
                    canLogin = isValidUsername && isValidPin
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> login()
            else -> Unit
        }
    }

    private fun login() {
        //
    }

}