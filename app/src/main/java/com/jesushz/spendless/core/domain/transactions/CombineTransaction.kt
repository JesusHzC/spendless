package com.jesushz.spendless.core.domain.transactions

import androidx.annotation.StringRes
import com.jesushz.spendless.core.database.entity.TransactionEntity

data class CombineTransaction(
    @StringRes val title: Int? = null,
    val date: String = "",
    val transactions: List<Transaction> = emptyList()
)
