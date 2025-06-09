package com.jesushz.spendless.transactions.presentation.all_transactions

import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.presentation.ui.UiText


sealed interface AllTransactionsAction {

    data object OnBack: AllTransactionsAction
    data object OnDismissTransactionClick: AllTransactionsAction
    data class OnEditTransaction(val transaction: Transaction): AllTransactionsAction
    data class OnDeleteRepeatTransaction(val transaction: Transaction): AllTransactionsAction
    data class OnDeleteTransaction(val transaction: Transaction): AllTransactionsAction
    data class OnError(val error: UiText): AllTransactionsAction
    data object OnExportTransactionClick: AllTransactionsAction
    data object OnDismissExportTransactionBottomSheet: AllTransactionsAction

}
