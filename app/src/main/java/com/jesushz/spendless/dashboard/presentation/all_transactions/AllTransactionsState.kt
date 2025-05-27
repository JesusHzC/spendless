package com.jesushz.spendless.dashboard.presentation.all_transactions

import com.jesushz.spendless.core.domain.transactions.CombineTransaction
import com.jesushz.spendless.core.domain.transactions.Currency
import com.jesushz.spendless.core.domain.transactions.DecimalSeparator
import com.jesushz.spendless.core.domain.transactions.ExpenseFormat
import com.jesushz.spendless.core.domain.transactions.ThousandSeparator
import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.util.PrefsUtil

data class AllTransactionsState(
    val username: String = "",
    val currency: Currency = Currency.MEXICAN_PESO,
    val expenseFormat: ExpenseFormat = ExpenseFormat.NEGATIVE,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.POINT,
    val thousandSeparator: ThousandSeparator = ThousandSeparator.COMMA,
    val allTransactions: List<CombineTransaction> = emptyList(),
    val showCreateTransactionBottomSheet: Boolean = false,
    val tmpTransaction: Transaction? = null
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
