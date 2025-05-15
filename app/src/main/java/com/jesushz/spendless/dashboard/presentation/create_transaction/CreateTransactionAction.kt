package com.jesushz.spendless.dashboard.presentation.create_transaction

import com.jesushz.spendless.core.domain.Category
import com.jesushz.spendless.core.domain.Repeat
import com.jesushz.spendless.core.domain.TransactionType

sealed interface CreateTransactionAction {

    data object OnClose: CreateTransactionAction
    data class OnTransactionTypeSelected(val transaction: TransactionType): CreateTransactionAction
    data class OnReceiverChange(val receiver: String): CreateTransactionAction
    data class OnAmountChange(val amount: String): CreateTransactionAction
    data class OnNoteChange(val note: String): CreateTransactionAction
    data class OnCategorySelected(val category: Category): CreateTransactionAction
    data class OnRepeatSelected(val repeat: Repeat): CreateTransactionAction

}
