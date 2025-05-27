package com.jesushz.spendless.auth.presentation.login

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.R
import com.jesushz.spendless.auth.domain.UsernameValidator
import com.jesushz.spendless.auth.domain.repository.AuthRepository
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.security.Biometrics
import com.jesushz.spendless.core.domain.user.User
import com.jesushz.spendless.core.presentation.ui.UiText
import com.jesushz.spendless.core.presentation.ui.asUiText
import com.jesushz.spendless.core.util.Constants.PIN_MAX_LENGTH
import com.jesushz.spendless.core.util.Constants.USERNAME_MAX_LENGTH
import com.jesushz.spendless.core.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private var tempUser: User? = null

    init {
        dataStoreManager
            .getBiometrics()
            .distinctUntilChanged()
            .onEach { biometrics ->
                val isEnabled = when (biometrics) {
                    Biometrics.ENABLE -> true
                    Biometrics.DISABLE -> false
                }
                _state.update {
                    it.copy(
                        biometricsEnabled = isEnabled
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> login()
            is LoginAction.OnUsernameChange -> {
                if (action.username.text.length <= USERNAME_MAX_LENGTH) {
                    _state.update {
                        it.copy(
                            username = action.username
                        )
                    }
                }
                validateFields()
            }
            is LoginAction.OnPinChange -> {
                if (action.pin.text.length <= PIN_MAX_LENGTH) {
                    _state.update {
                        it.copy(
                            pin = action.pin
                        )
                    }
                }
                validateFields()
            }
            LoginAction.OnBiometricsSuccess -> {
                confirmLogin()
            }
            is LoginAction.OnBiometricsError -> {
                viewModelScope.launch {
                    _event.send(LoginEvent.OnError(action.error))
                    if (action.confirmLogout) {
                        confirmLogin()
                    }
                }
            }
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
                        tempUser = result.data
                        withContext(Dispatchers.Main) {
                            if (state.value.biometricsEnabled) {
                                _event.send(LoginEvent.OnRequestBiometrics)
                            } else {
                                confirmLogin()
                            }
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

    private fun validateFields() {
        val username = state.value.username.text
        val pin = state.value.pin.text

        val isValidUsername = validator.validate(username)
        val isValidPin = pin.length == PIN_MAX_LENGTH && pin.isDigitsOnly()

        _state.update {
            it.copy(
                canLogin = isValidUsername && isValidPin
            )
        }
    }

    private fun confirmLogin() {
        if (tempUser == null) return

        viewModelScope.launch {
            dataStoreManager.saveUser(tempUser!!)
            dataStoreManager.updateSessionMonitorEnabled(true)
            tempUser = null
            _event.send(LoginEvent.OnLoginSuccess)
        }
    }

}