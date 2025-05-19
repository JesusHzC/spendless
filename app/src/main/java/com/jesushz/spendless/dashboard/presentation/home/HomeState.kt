package com.jesushz.spendless.dashboard.presentation.home

import com.jesushz.spendless.core.database.entity.TransactionEntity
import com.jesushz.spendless.core.domain.transactions.CombineTransaction
import com.jesushz.spendless.core.domain.transactions.Currency
import com.jesushz.spendless.core.domain.transactions.DecimalSeparator
import com.jesushz.spendless.core.domain.transactions.ExpenseFormat
import com.jesushz.spendless.core.domain.transactions.ThousandSeparator
import com.jesushz.spendless.core.util.PrefsUtil

data class HomeState(
    val showCreateTransactionBottomSheet: Boolean = false,
    val username: String = "",
    val latestTransactions: List<CombineTransaction> = emptyList(),
    val accountBalance: Double = 0.0,
    val previousWeekBalance: Double = 0.0,
    val longestTransaction: TransactionEntity? = null,
    val latestTransaction: TransactionEntity? = null,
    val currency: Currency = Currency.MEXICAN_PESO,
    val expenseFormat: ExpenseFormat = ExpenseFormat.NEGATIVE,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.POINT,
    val thousandSeparator: ThousandSeparator = ThousandSeparator.COMMA
) {
    fun formatAmount(amount: Number): String {
        return PrefsUtil.formatAmount(
            amount = amount,
            currency = currency,
            expenseFormat = expenseFormat,
            decimalSeparator = decimalSeparator,
            thousandSeparator = thousandSeparator
        )
    }
}
