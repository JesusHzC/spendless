package com.jesushz.spendless.dashboard.presentation.dashboard

import com.jesushz.spendless.core.domain.transactions.CombineTransaction
import com.jesushz.spendless.core.domain.transactions.Currency
import com.jesushz.spendless.core.domain.transactions.DecimalSeparator
import com.jesushz.spendless.core.domain.transactions.ExpenseFormat
import com.jesushz.spendless.core.domain.transactions.ThousandSeparator
import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.util.PrefsUtil

data class DashboardState(
    val showCreateTransactionBottomSheet: Boolean = false,
    val username: String = "",
    val latestTransactions: List<CombineTransaction> = emptyList(),
    val accountBalance: Double = 0.0,
    val previousWeekBalance: Double = 0.0,
    val longestTransaction: Transaction? = null,
    val latestTransaction: Transaction? = null,
    val currency: Currency = Currency.MEXICAN_PESO,
    val expenseFormat: ExpenseFormat = ExpenseFormat.NEGATIVE,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.POINT,
    val thousandSeparator: ThousandSeparator = ThousandSeparator.COMMA,
    val isLoading: Boolean = true,
    val tmpTransaction: Transaction? = null,
    val shouldShowBalance: Boolean = false,
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
