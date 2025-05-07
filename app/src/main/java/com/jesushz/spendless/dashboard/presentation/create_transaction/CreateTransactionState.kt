package com.jesushz.spendless.dashboard.presentation.create_transaction

import com.jesushz.spendless.dashboard.domain.TransactionType

data class CreateTransactionState(
    val transactionType: TransactionType = TransactionType.EXPENSE,
)
