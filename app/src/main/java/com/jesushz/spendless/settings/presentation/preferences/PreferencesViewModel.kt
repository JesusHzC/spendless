package com.jesushz.spendless.settings.presentation.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.preferences.TransactionsPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreferencesViewModel(
    private val dataStoreManager: DataStoreManager,
    private val applicationScope: CoroutineScope
): ViewModel() {

    private val _state = MutableStateFlow(PreferencesState())
    val state = _state.asStateFlow()

    private val _event = Channel<PreferencesEvent>()
    val event = _event.receiveAsFlow()

    init {
        initPreferences()
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

    private fun initPreferences() {
        viewModelScope.launch {
            val transactionsPreferences =
                dataStoreManager.getAllTransactionsPreferences()

            _state.update { oldState ->
                val newState = oldState.copy(
                    currency = transactionsPreferences.currency,
                    expenseFormat = transactionsPreferences.expenseFormat,
                    decimalSeparator = transactionsPreferences.decimalSeparator,
                    thousandSeparator = transactionsPreferences.thousandSeparator,
                )
                newState.copy(
                    totalSpendFormat = newState.formatAmount()
                )
            }
        }
    }

    private suspend fun savePreferences() {
        applicationScope.launch {
            val transactionsPreferences = TransactionsPreferences(
                currency = state.value.currency,
                expenseFormat = state.value.expenseFormat,
                decimalSeparator = state.value.decimalSeparator,
                thousandSeparator = state.value.thousandSeparator
            )
            dataStoreManager.saveAllTransactionsPreferences(transactionsPreferences)
        }.join()
    }

    private fun onStartTracking() {
        viewModelScope.launch {
            _event.send(PreferencesEvent.OnSavePreferencesSuccess)
            savePreferences()
        }
    }

}
