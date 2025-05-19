package com.jesushz.spendless.dashboard.presentation.all_transactions

sealed interface AllTransactionsAction {

    data object OnBack: AllTransactionsAction

}
