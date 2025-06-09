package com.jesushz.spendless.transactions.presentation.export_transactions

import com.jesushz.spendless.core.presentation.ui.UiText

sealed interface ExportTransactionsEvent {

    data object OnExportSuccess : ExportTransactionsEvent
    data class OnExportError(val error: UiText) : ExportTransactionsEvent

}
