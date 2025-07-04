package com.jesushz.spendless.settings.presentation.preferences

import com.jesushz.spendless.core.domain.preferences.PrefsFlow
import com.jesushz.spendless.core.domain.transactions.Currency
import com.jesushz.spendless.core.domain.transactions.DecimalSeparator
import com.jesushz.spendless.core.domain.transactions.ExpenseFormat
import com.jesushz.spendless.core.domain.transactions.ThousandSeparator
import com.jesushz.spendless.core.util.PrefsUtil

data class PreferencesState(
    val totalSpend: Double = 10000.0,
    val totalSpendFormat: String = "",
    val expenseFormat: ExpenseFormat = ExpenseFormat.POSITIVE,
    val currency: Currency = Currency.MEXICAN_PESO,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.POINT,
    val thousandSeparator: ThousandSeparator = ThousandSeparator.SPACE,
    val flow: PrefsFlow = PrefsFlow.SETTINGS
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
