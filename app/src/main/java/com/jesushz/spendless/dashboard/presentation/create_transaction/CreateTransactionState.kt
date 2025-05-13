package com.jesushz.spendless.dashboard.presentation.create_transaction

import com.jesushz.spendless.dashboard.domain.TransactionType

data class CreateTransactionState(
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val receiver: String = "",
    val amount: String = "",
    val note: String = "",
)
