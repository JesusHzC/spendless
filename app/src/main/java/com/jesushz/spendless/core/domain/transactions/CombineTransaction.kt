package com.jesushz.spendless.core.domain.transactions

import androidx.annotation.StringRes
import com.jesushz.spendless.core.database.entity.TransactionEntity

data class CombineTransaction(
    @StringRes val title: Int,
    val transactions: List<TransactionEntity> = emptyList()
)
