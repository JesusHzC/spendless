package com.jesushz.spendless.auth.presentation.register

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.R
import com.jesushz.spendless.auth.domain.UsernameValidator
import com.jesushz.spendless.auth.domain.repository.AuthRepository
import com.jesushz.spendless.core.presentation.ui.UiText
import com.jesushz.spendless.core.presentation.ui.asUiText
import com.jesushz.spendless.core.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(
    private val validator: UsernameValidator,
    private val repository: AuthRepository
): ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private val _event = Channel<RegisterEvent>()
    val event = _event.receiveAsFlow()

    private val usernameText = snapshotFlow {
        state.value.username.text
    }

    init {
        usernameText
            .onEach { text ->
                _state.update {
                    it.copy(
                        isUsernameValid = validator.validate(text)
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: RegisterAction) {
        when (action) {
            RegisterAction.OnNextButtonClick -> {
                validateUsername()
            }
            else -> Unit
        }
    }

    private fun validateUsername() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository
                .checkIfUsernameExists(state.value.username.text.toString())
            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Error -> {
                        _event.send(RegisterEvent.OnError(result.error.asUiText()))
                    }
                    is Result.Success -> {
                        if (result.data) {
                            _event.send(
                                RegisterEvent.OnError(
                                    UiText.StringResource(
                                        R.string.error_constraint_violation
                                    )
                                )
                            )
                        } else {
                            _event.send(RegisterEvent.OnUsernameSuccess)
                        }
                    }
                }
            }
        }
    }

}
