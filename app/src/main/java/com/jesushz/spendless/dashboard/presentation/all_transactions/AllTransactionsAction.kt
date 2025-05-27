package com.jesushz.spendless.dashboard.presentation.all_transactions

import com.jesushz.spendless.core.domain.transactions.Transaction


sealed interface AllTransactionsAction {

    data object OnBack: AllTransactionsAction
    data object OnDismissTransactionClick: AllTransactionsAction
    data class OnEditTransaction(val transaction: Transaction): AllTransactionsAction
    data class OnDeleteTransaction(val transaction: Transaction): AllTransactionsAction

}
