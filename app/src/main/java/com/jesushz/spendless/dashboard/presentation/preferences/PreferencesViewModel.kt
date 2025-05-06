package com.jesushz.spendless.dashboard.presentation.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.dashboard.domain.Preferences
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreferencesViewModel(
    private val preferences: Preferences
): ViewModel() {

    private val _state = MutableStateFlow(PreferencesState())
    val state = _state.asStateFlow()

    private val _event = Channel<PreferencesEvent>()
    val event = _event.receiveAsFlow()

    init {
        viewModelScope.launch {
            preferences.initialize()
            _state.update {
                it.copy(
                    expenseFormat = preferences.expenseFormat,
                    currency = preferences.currency,
                    decimalSeparator = preferences.decimalSeparator,
                    thousandSeparator = preferences.thousandSeparator,
                    totalSpendFormat = preferences.formatAmount(state.value.totalSpend)
                )
            }
        }
    }

    fun onAction(action: PreferencesAction) {
        when (action) {
            is PreferencesAction.OnExpenseFormatSelected -> {
                preferences.updateExpenseFormat(action.format)
                _state.update {
                    it.copy(
                        expenseFormat = action.format,
                        totalSpendFormat = preferences.formatAmount(state.value.totalSpend)
                    )
                }
            }
            is PreferencesAction.OnCurrencySelected -> {
                preferences.updateCurrency(action.currency)
                _state.update {
                    it.copy(
                        currency = action.currency,
                        totalSpendFormat = preferences.formatAmount(state.value.totalSpend)
                    )
                }
            }
            is PreferencesAction.OnDecimalSeparatorSelected -> {
                preferences.updateDecimalSeparator(action.separator)
                _state.update {
                    it.copy(
                        decimalSeparator = action.separator,
                        totalSpendFormat = preferences.formatAmount(state.value.totalSpend)
                    )
                }
            }
            is PreferencesAction.OnThousandSeparatorSelected -> {
                preferences.updateThousandSeparator(action.separator)
                _state.update {
                    it.copy(
                        thousandSeparator = action.separator,
                        totalSpendFormat = preferences.formatAmount(state.value.totalSpend)
                    )
                }
            }
            PreferencesAction.OnStartTrackingClick -> {
                onStartTracking()
            }
            else -> Unit
        }
    }

    private fun onStartTracking() {
        viewModelScope.launch {
            preferences.savePreferences()
            _event.send(PreferencesEvent.OnSavePreferencesSuccess)
        }
    }

}
