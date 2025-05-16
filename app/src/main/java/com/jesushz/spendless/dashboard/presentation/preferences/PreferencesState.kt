package com.jesushz.spendless.dashboard.presentation.preferences

import com.jesushz.spendless.core.domain.Currency
import com.jesushz.spendless.core.domain.DecimalSeparator
import com.jesushz.spendless.core.domain.ExpenseFormat
import com.jesushz.spendless.core.domain.ThousandSeparator
import com.jesushz.spendless.core.util.PrefsUtil

data class PreferencesState(
    val totalSpend: Double = 10000.0,
    val totalSpendFormat: String = "",
    val expenseFormat: ExpenseFormat = ExpenseFormat.POSITIVE,
    val currency: Currency = Currency.MEXICAN_PESO,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.POINT,
    val thousandSeparator: ThousandSeparator = ThousandSeparator.SPACE,
) {
    fun formatAmount(): String {
        return PrefsUtil.formatAmount(
            totalSpend,
            currency,
            expenseFormat,
            decimalSeparator,
            thousandSeparator
        )
    }
}
