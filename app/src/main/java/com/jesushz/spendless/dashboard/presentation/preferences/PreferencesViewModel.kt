package com.jesushz.spendless.dashboard.presentation.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
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
        viewModelScope.launch {
            initPreferences()
        }
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

    private suspend fun initPreferences() {
        applicationScope.launch {
            val currency = async { dataStoreManager.getCurrency() }
            val expenseFormat = async { dataStoreManager.getExpenseFormat() }
            val decimalSeparator = async { dataStoreManager.getDecimalSeparator() }
            val thousandSeparator = async { dataStoreManager.getThousandSeparator() }

            _state.update { oldState ->
                val newState = oldState.copy(
                    currency = currency.await(),
                    expenseFormat = expenseFormat.await(),
                    decimalSeparator = decimalSeparator.await(),
                    thousandSeparator = thousandSeparator.await(),
                )
                newState.copy(
                    totalSpendFormat = newState.formatAmount()
                )
            }
        }.join()
    }

    private suspend fun savePreferences() {
        applicationScope.launch {
            dataStoreManager.saveCurrency(state.value.currency)
            dataStoreManager.saveExpenseFormat(state.value.expenseFormat)
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
