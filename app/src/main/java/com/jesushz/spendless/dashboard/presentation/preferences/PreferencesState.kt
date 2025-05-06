package com.jesushz.spendless.dashboard.presentation.preferences

import com.jesushz.spendless.dashboard.domain.Currency
import com.jesushz.spendless.dashboard.domain.DecimalSeparator
import com.jesushz.spendless.dashboard.domain.ExpenseFormat
import com.jesushz.spendless.dashboard.domain.ThousandSeparator

data class PreferencesState(
    val totalSpend: Double = 10000.0,
    val totalSpendFormat: String = "",
    val expenseFormat: ExpenseFormat = ExpenseFormat.POSITIVE,
    val currency: Currency = Currency.MEXICAN_PESO,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.POINT,
    val thousandSeparator: ThousandSeparator = ThousandSeparator.SPACE,
)
