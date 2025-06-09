package com.jesushz.spendless.transactions.presentation.export_transactions

import com.jesushz.spendless.core.domain.transactions.Export

sealed interface ExportTransactionsAction {

    data object OnClose: ExportTransactionsAction
    data class OnExportSelected(val export: Export): ExportTransactionsAction
    data object OnExportTransactions: ExportTransactionsAction

}
