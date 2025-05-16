package com.jesushz.spendless.core.domain.preferences

import com.jesushz.spendless.core.domain.Currency
import com.jesushz.spendless.core.domain.DecimalSeparator
import com.jesushz.spendless.core.domain.ExpenseFormat
import com.jesushz.spendless.core.domain.ThousandSeparator

data class TransactionsPreferences(
    val expenseFormat: ExpenseFormat = ExpenseFormat.POSITIVE,
    val currency: Currency = Currency.MEXICAN_PESO,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.POINT,
    val thousandSeparator: ThousandSeparator = ThousandSeparator.SPACE,
)
