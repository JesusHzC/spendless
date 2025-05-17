package com.jesushz.spendless.dashboard.presentation.create_transaction

import com.jesushz.spendless.core.domain.Category
import com.jesushz.spendless.core.domain.Repeat
import com.jesushz.spendless.core.domain.TransactionType
import com.jesushz.spendless.core.domain.preferences.TransactionsPreferences

data class CreateTransactionState(
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val receiver: String = "",
    val amount: String = "",
    val note: String = "",
    val categorySelected: Category = Category.OTHER,
    val repeatSelected: Repeat = Repeat.NOT_REPEAT,
    val transactionsPreferences: TransactionsPreferences = TransactionsPreferences()
)
