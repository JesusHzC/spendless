package com.jesushz.spendless.core.domain.preferences

import com.jesushz.spendless.core.domain.transactions.Currency
import com.jesushz.spendless.core.domain.transactions.DecimalSeparator
import com.jesushz.spendless.core.domain.transactions.ExpenseFormat
import com.jesushz.spendless.core.domain.transactions.ThousandSeparator

data class TransactionsPreferences(
    val expenseFormat: ExpenseFormat = ExpenseFormat.POSITIVE,
    val currency: Currency = Currency.MEXICAN_PESO,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.POINT,
    val thousandSeparator: ThousandSeparator = ThousandSeparator.SPACE,
)
