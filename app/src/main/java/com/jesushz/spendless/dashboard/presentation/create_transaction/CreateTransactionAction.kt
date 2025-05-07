package com.jesushz.spendless.dashboard.presentation.create_transaction

import com.jesushz.spendless.dashboard.domain.TransactionType

sealed interface CreateTransactionAction {

    data object OnClose: CreateTransactionAction
    data class OnTransactionTypeSelected(val transaction: TransactionType): CreateTransactionAction

}
