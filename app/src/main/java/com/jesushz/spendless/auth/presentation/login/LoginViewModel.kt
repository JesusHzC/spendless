package com.jesushz.spendless.auth.presentation.login

import androidx.compose.runtime.snapshotFlow
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.R
import com.jesushz.spendless.auth.domain.UsernameValidator
import com.jesushz.spendless.auth.domain.repository.AuthRepository
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.presentation.ui.UiText
import com.jesushz.spendless.core.presentation.ui.asUiText
import com.jesushz.spendless.core.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val validator: UsernameValidator,
    private val repository: AuthRepository,
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _event = Channel<LoginEvent>()
    val event = _event.receiveAsFlow()

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
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.login(
                state.value.username.text.toString().trim(),
                state.value.pin.text.toString()
            )
            when(result) {
                is Result.Error -> {
                    withContext(Dispatchers.Main) {
                        _event.send(LoginEvent.OnError(result.error.asUiText()))
                    }
                }
                is Result.Success -> {
                    if (result.data != null) {
                        dataStoreManager.saveUser(result.data)
                        withContext(Dispatchers.Main) {
                            _event.send(LoginEvent.OnLoginSuccess)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            _event.send(LoginEvent.OnError(UiText.StringResource(R.string.error_login)))
                        }
                    }
                }
            }
        }
    }

}