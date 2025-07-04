package com.jesushz.spendless.transactions.presentation.create_transaction

import com.jesushz.spendless.core.domain.transactions.Category
import com.jesushz.spendless.core.domain.transactions.Repeat
import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.domain.transactions.TransactionType

sealed interface CreateTransactionAction {

    data object OnClose: CreateTransactionAction
    data class OnTransactionTypeSelected(val transaction: TransactionType): CreateTransactionAction
    data class OnReceiverChange(val receiver: String): CreateTransactionAction
    data class OnAmountChange(val amount: String): CreateTransactionAction
    data class OnNoteChange(val note: String): CreateTransactionAction
    data class OnCategorySelected(val category: Category): CreateTransactionAction
    data class OnRepeatSelected(val repeat: Repeat): CreateTransactionAction
    data object OnCreateTransactionClick: CreateTransactionAction
    data class OnDateSelected(val date: String): CreateTransactionAction
    data class OnUpdateTransaction(val transaction: Transaction): CreateTransactionAction

}
