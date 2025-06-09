package com.jesushz.spendless.transactions.presentation.dashboard

import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.presentation.ui.UiText

sealed interface DashboardAction {

    data object OnCreateTransactionClick: DashboardAction
    data object OnDismissTransactionClick: DashboardAction
    data object OnShowAllTransactions: DashboardAction
    data object OnSettingsClick: DashboardAction
    data class OnEditTransaction(val transaction: Transaction): DashboardAction
    data class OnDeleteTransaction(val transaction: Transaction): DashboardAction
    data object OnShowBalanceClick: DashboardAction
    data object OnExportTransactionClick: DashboardAction
    data object OnDismissExportTransactionBottomSheet: DashboardAction
    data class OnError(val error: UiText): DashboardAction

}
