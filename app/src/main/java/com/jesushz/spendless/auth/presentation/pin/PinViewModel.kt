package com.jesushz.spendless.auth.presentation.pin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jesushz.spendless.R
import com.jesushz.spendless.auth.domain.PinFlow
import com.jesushz.spendless.auth.presentation.pin.components.Keys
import com.jesushz.spendless.core.presentation.ui.UiText
import com.jesushz.spendless.core.util.Routes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class PinViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(PinState())
    val state = _state.asStateFlow()

    private val _event = Channel<PinEvent>()
    val event = _event.receiveAsFlow()

    init {
        _state.update {
            it.copy(
                flow = savedStateHandle.toRoute<Routes.PinScreen>().flow
            )
        }
        Timber.i("Pin flow: ${state.value.flow}")
    }

    fun onAction(action: PinAction) {
        when (action) {
            is PinAction.OnKeyPressed -> {
                onKeyPressed(action.key)
            }
            else -> Unit
        }
    }

    private fun onKeyPressed(key: Keys) {
        when (key) {
            Keys.BLANK -> Unit
            Keys.DELETE -> {
                when (state.value.flow) {
                    PinFlow.CONFIRM -> {
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
                    PinFlow.CONFIRM -> {
                        if (state.value.confirmPin.length < PIN_LENGTH) {
                            _state.update {
                                it.copy(confirmPin = state.value.confirmPin + key.value)
                            }
                        }
                        if (state.value.confirmPin.length == PIN_LENGTH) {
                            if (state.value.pin == state.value.confirmPin) {
                                Timber.i("Pin is valid")
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
                                    it.copy(flow = PinFlow.CONFIRM)
                                }
                            } else {
                                Timber.i("Validate pin")
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val PIN_LENGTH = 5
    }

}
