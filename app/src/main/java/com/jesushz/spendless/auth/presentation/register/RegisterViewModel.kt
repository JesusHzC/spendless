package com.jesushz.spendless.auth.presentation.register

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.auth.domain.UsernameValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class RegisterViewModel(
    private val validator: UsernameValidator
): ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

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

}
