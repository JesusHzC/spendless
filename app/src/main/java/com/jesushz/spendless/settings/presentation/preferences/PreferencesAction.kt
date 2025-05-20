package com.jesushz.spendless.settings.presentation.preferences

import com.jesushz.spendless.core.domain.transactions.Currency
import com.jesushz.spendless.core.domain.transactions.DecimalSeparator
import com.jesushz.spendless.core.domain.transactions.ExpenseFormat
import com.jesushz.spendless.core.domain.transactions.ThousandSeparator

sealed interface PreferencesAction {

    data class OnExpenseFormatSelected(val format: ExpenseFormat): PreferencesAction
    data class OnCurrencySelected(val currency: Currency): PreferencesAction
    data class OnDecimalSeparatorSelected(val separator: DecimalSeparator): PreferencesAction
    data class OnThousandSeparatorSelected(val separator: ThousandSeparator): PreferencesAction
    data object OnStartTrackingClick: PreferencesAction
    data object OnNavigateBack: PreferencesAction

}
