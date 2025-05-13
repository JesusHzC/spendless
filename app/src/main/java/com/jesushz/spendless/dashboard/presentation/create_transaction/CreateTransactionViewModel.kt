package com.jesushz.spendless.dashboard.presentation.create_transaction

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateTransactionViewModel: ViewModel() {

    private val _state = MutableStateFlow(CreateTransactionState())
    val state = _state.asStateFlow()

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
            else -> {
                Unit
            }
        }
    }

}
