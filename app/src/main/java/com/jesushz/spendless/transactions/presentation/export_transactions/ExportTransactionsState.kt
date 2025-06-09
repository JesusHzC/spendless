package com.jesushz.spendless.transactions.presentation.export_transactions

import androidx.compose.runtime.Stable
import com.jesushz.spendless.core.domain.transactions.Export
import com.jesushz.spendless.core.domain.transactions.Transaction

@Stable
data class ExportTransactionsState(
    val exportSelected: Export = Export.LAST_THREE_MONTHS,
    val transactionsToExport: List<Transaction> = emptyList(),
    val userId: String? = null,
)
