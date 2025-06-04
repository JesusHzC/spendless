package com.jesushz.spendless.dashboard.presentation.dashboard

import com.jesushz.spendless.core.domain.transactions.Transaction

sealed interface DashboardAction {

    data object OnCreateTransactionClick: DashboardAction
    data object OnDismissTransactionClick: DashboardAction
    data object OnShowAllTransactions: DashboardAction
    data object OnSettingsClick: DashboardAction
    data class OnEditTransaction(val transaction: Transaction): DashboardAction
    data class OnDeleteTransaction(val transaction: Transaction): DashboardAction
    data object OnShowBalanceClick: DashboardAction

}
