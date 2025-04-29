package com.jesushz.spendless.auth.presentation.pin

import androidx.lifecycle.ViewModel
import com.jesushz.spendless.auth.presentation.pin.components.Keys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PinViewModel: ViewModel() {

    private val _state = MutableStateFlow(PinState())
    val state = _state.asStateFlow()

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
                val pin = state.value.pin
                if (pin.isNotEmpty()) {
                    _state.update {
                        it.copy(
                            pin = pin.dropLast(1)
                        )
                    }
                }
            }
            else -> {
                val pin = state.value.pin
                if (pin.length < PIN_LENGTH) {
                    _state.update {
                        it.copy(
                            pin = pin + key.value
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val PIN_LENGTH = 5
    }

}
