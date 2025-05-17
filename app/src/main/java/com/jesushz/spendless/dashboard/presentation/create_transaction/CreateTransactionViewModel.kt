package com.jesushz.spendless.dashboard.presentation.create_transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateTransactionViewModel(
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    private val _state = MutableStateFlow(CreateTransactionState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    transactionsPreferences = dataStoreManager.getAllTransactionsPreferences()
                )
            }
        }
    }

    fun onAction(action: CreateTransactionAction) {
        when (action) {
            is CreateTransactionAction.OnAmountChange -> {
                _state.update {
                    it.copy(
                        amount = action.amount
                    )
                }
            }
            is CreateTransactionAction.OnReceiverChange -> {
                _state.update {
                    it.copy(
                        receiver = action.receiver
                    )
                }
            }
            is CreateTransactionAction.OnNoteChange -> {
                _state.update {
                    it.copy(
                        note = action.note
                    )
                }
            }
            is CreateTransactionAction.OnTransactionTypeSelected -> {
                _state.update {
                    it.copy(
                        transactionType = action.transaction
                    )
                }
            }
            is CreateTransactionAction.OnCategorySelected -> {
                _state.update {
                    it.copy(
                        categorySelected = action.category
                    )
                }
            }
            is CreateTransactionAction.OnRepeatSelected -> {
                _state.update {
                    it.copy(
                        repeatSelected = action.repeat
                    )
                }
            }
            else -> {
                Unit
            }
        }
    }

}
