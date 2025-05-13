package com.jesushz.spendless.dashboard.presentation.create_transaction

import com.jesushz.spendless.dashboard.domain.TransactionType

sealed interface CreateTransactionAction {

    data object OnClose: CreateTransactionAction
    data class OnTransactionTypeSelected(val transaction: TransactionType): CreateTransactionAction
    data class OnReceiverChange(val receiver: String): CreateTransactionAction
    data class OnAmountChange(val amount: String): CreateTransactionAction
    data class OnNoteChange(val note: String): CreateTransactionAction

}
