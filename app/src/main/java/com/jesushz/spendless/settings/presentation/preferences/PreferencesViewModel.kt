package com.jesushz.spendless.settings.presentation.preferences

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.util.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreferencesViewModel(
    private val dataStoreManager: DataStoreManager,
    private val applicationScope: CoroutineScope,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(PreferencesState())
    val state = _state.asStateFlow()

    private val _event = Channel<PreferencesEvent>()
    val event = _event.receiveAsFlow()

    init {
        _state.update {
            it.copy(
                flow = savedStateHandle.toRoute<Routes.PreferencesScreen>().flow
            )
        }

        dataStoreManager
            .getCurrency()
            .onEach { currency ->
                _state.update { oldState ->
                    val newState = oldState.copy(
                        currency = currency
                    )
                    newState.copy(
                        totalSpendFormat = newState.formatAmount()
                    )
                }
            }
            .launchIn(viewModelScope)

        dataStoreManager
            .getExpenseFormat()
            .onEach { format ->
                _state.update { oldState ->
                    val newState = oldState.copy(
                        expenseFormat = format
                    )
                    newState.copy(
                        totalSpendFormat = newState.formatAmount()
                    )
                }
            }
            .launchIn(viewModelScope)

        dataStoreManager
            .getDecimalSeparator()
            .onEach { separator ->
                _state.update { oldState ->
                    val newState = oldState.copy(
                        decimalSeparator = separator
                    )
                    newState.copy(
                        totalSpendFormat = newState.formatAmount()
                    )
                }
            }
            .launchIn(viewModelScope)

        dataStoreManager
            .getThousandSeparator()
            .onEach { separator ->
                _state.update { oldState ->
                    val newState = oldState.copy(
                        thousandSeparator = separator
                    )
                    newState.copy(
                        totalSpendFormat = newState.formatAmount()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: PreferencesAction) {
        when (action) {
            is PreferencesAction.OnExpenseFormatSelected -> {
                _state.update { oldState ->
                    val newState = oldState.copy(
                        expenseFormat = action.format
                    )
                    newState.copy(
                        totalSpendFormat = newState.formatAmount()
                    )
                }
            }
            is PreferencesAction.OnCurrencySelected -> {
                _state.update { oldState ->
                    val newState = oldState.copy(
                        currency = action.currency
                    )
                    newState.copy(
                        totalSpendFormat = newState.formatAmount()
                    )
                }
            }
            is PreferencesAction.OnDecimalSeparatorSelected -> {
                _state.update { oldState ->
                    val newState = oldState.copy(
                        decimalSeparator = action.separator
                    )
                    newState.copy(
                        totalSpendFormat = newState.formatAmount()
                    )
                }
            }
            is PreferencesAction.OnThousandSeparatorSelected -> {
                _state.update { oldState ->
                    val newState = oldState.copy(
                        thousandSeparator = action.separator
                    )
                    newState.copy(
                        totalSpendFormat = newState.formatAmount()
                    )
                }
            }
            PreferencesAction.OnStartTrackingClick -> {
                onStartTracking()
            }
            else -> Unit
        }
    }

    private suspend fun savePreferences() {
        applicationScope.launch {
            dataStoreManager.saveExpenseFormat(state.value.expenseFormat)
            dataStoreManager.saveCurrency(state.value.currency)
            dataStoreManager.saveDecimalSeparator(state.value.decimalSeparator)
            dataStoreManager.saveThousandSeparator(state.value.thousandSeparator)
        }.join()
    }

    private fun onStartTracking() {
        viewModelScope.launch {
            _event.send(PreferencesEvent.OnSavePreferencesSuccess)
            savePreferences()
        }
    }

}
