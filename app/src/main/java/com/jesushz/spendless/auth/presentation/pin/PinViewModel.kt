package com.jesushz.spendless.auth.presentation.pin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jesushz.spendless.R
import com.jesushz.spendless.auth.domain.PinFlow
import com.jesushz.spendless.auth.domain.repository.AuthRepository
import com.jesushz.spendless.auth.presentation.pin.components.Keys
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.security.Biometrics
import com.jesushz.spendless.core.domain.user.User
import com.jesushz.spendless.core.presentation.ui.UiText
import com.jesushz.spendless.core.presentation.ui.asUiText
import com.jesushz.spendless.core.util.Result
import com.jesushz.spendless.core.util.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PinViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: AuthRepository,
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    private val _state = MutableStateFlow(PinState())
    val state = _state.asStateFlow()

    private val _event = Channel<PinEvent>()
    val event = _event.receiveAsFlow()

    init {
        _state.update {
            it.copy(
                flow = savedStateHandle.toRoute<Routes.PinScreen>().flow,
                username = savedStateHandle.toRoute<Routes.PinScreen>().username.orEmpty()
            )
        }

        dataStoreManager
            .getUser()
            .mapNotNull { it }
            .onEach { user ->
                _state.update {
                    it.copy(
                        pinSaved = user.pin,
                        username = user.username
                    )
                }
            }
            .launchIn(viewModelScope)

        dataStoreManager
            .getBiometrics()
            .distinctUntilChanged()
            .onEach { biometrics ->
                val isRefreshLogin = state.value.flow == PinFlow.REFRESH_LOGIN
                val biometricsEnabled = biometrics == Biometrics.ENABLE && isRefreshLogin
                _state.update {
                    it.copy(
                        isBiometricEnabled = biometricsEnabled,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: PinAction) {
        when (action) {
            is PinAction.OnKeyPressed -> {
                onKeyPressed(action.key)
            }
            PinAction.OnBackPressed -> {
                when (state.value.flow) {
                    PinFlow.REGISTER -> {
                        viewModelScope.launch {
                            _event.send(PinEvent.OnNavigateBack)
                        }
                    }
                    PinFlow.CONFIRM_REGISTER -> {
                        _state.update {
                            it.copy(
                                flow = PinFlow.REGISTER,
                                confirmPin = "",
                                pin = ""
                            )
                        }
                    }
                    PinFlow.REFRESH_LOGIN -> Unit
                }
            }
            is PinAction.OnBiometricsError -> {
                viewModelScope.launch {
                    _event.send(PinEvent.OnError(action.error))
                }
            }
            PinAction.OnBiometricsSuccess -> {
                viewModelScope.launch {
                    dataStoreManager.updateLockOutEnabled(true)
                    _event.send(PinEvent.OnRefreshLoginSuccess)
                }
            }
        }
    }

    private fun onKeyPressed(key: Keys) {
        when (key) {
            Keys.BLANK -> {
                if (state.value.isBiometricEnabled) {
                    viewModelScope.launch {
                        _event.send(PinEvent.OnBiometricLogin)
                    }
                }
            }
            Keys.DELETE -> {
                when (state.value.flow) {
                    PinFlow.CONFIRM_REGISTER -> {
                        if (state.value.confirmPin.isNotEmpty()) {
                            _state.update {
                                it.copy(confirmPin = state.value.confirmPin.dropLast(1))
                            }
                        }
                    }
                    else -> {
                        if (state.value.pin.isNotEmpty()) {
                            _state.update {
                                it.copy(pin = state.value.pin.dropLast(1))
                            }
                        }
                    }
                }
            }
            else -> {
                when (state.value.flow) {
                    PinFlow.CONFIRM_REGISTER -> {
                        if (state.value.confirmPin.length < PIN_LENGTH) {
                            _state.update {
                                it.copy(confirmPin = state.value.confirmPin + key.value)
                            }
                        }
                        if (state.value.confirmPin.length == PIN_LENGTH) {
                            if (state.value.pin == state.value.confirmPin) {
                                register()
                            } else {
                                viewModelScope.launch {
                                    _event.send(PinEvent.OnError(UiText.StringResource(R.string.pin_not_match)))
                                }
                                _state.update {
                                    it.copy(
                                        confirmPin = "",
                                    )
                                }
                            }
                        }
                    }
                    else -> {
                        if (state.value.pin.length < PIN_LENGTH) {
                            _state.update {
                                it.copy(pin = state.value.pin + key.value)
                            }
                        }
                        if (state.value.pin.length == PIN_LENGTH) {
                            if (state.value.flow == PinFlow.REGISTER) {
                                _state.update {
                                    it.copy(flow = PinFlow.CONFIRM_REGISTER)
                                }
                            } else {
                                if (state.value.pin == state.value.pinSaved) {
                                    viewModelScope.launch {
                                        dataStoreManager.updateLockOutEnabled(true)
                                        _event.send(PinEvent.OnRefreshLoginSuccess)
                                    }
                                } else {
                                    viewModelScope.launch {
                                        _event.send(PinEvent.OnError(UiText.StringResource(R.string.wrong_pin)))
                                    }
                                    _state.update {
                                        it.copy(
                                            pin = "",
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun register() {
        if (state.value.username.isEmpty() || state.value.pin.isEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            val user = User(
                username = state.value.username,
                pin = state.value.pin
            )
            val result = repository
                .register(
                    user = user
                )

            when (result) {
                is Result.Error -> {
                    withContext(Dispatchers.Main) {
                        _event.send(PinEvent.OnError(result.error.asUiText()))
                    }
                }
                is Result.Success -> {
                    dataStoreManager.saveUser(user)
                    dataStoreManager.updateSessionMonitorEnabled(true)
                    withContext(Dispatchers.Main) {
                        _event.send(PinEvent.OnRegisterSuccess)
                    }
                }
            }
        }
    }

    companion object {
        const val PIN_LENGTH = 5
    }

}
