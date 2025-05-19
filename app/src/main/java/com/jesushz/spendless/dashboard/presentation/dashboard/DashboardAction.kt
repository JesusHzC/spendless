package com.jesushz.spendless.dashboard.presentation.dashboard

sealed interface DashboardAction {

    data object OnCreateTransactionClick: DashboardAction
    data object OnDismissTransactionClick: DashboardAction
    data object OnShowAllTransactions: DashboardAction
    data object OnSettingsClick: DashboardAction

}
