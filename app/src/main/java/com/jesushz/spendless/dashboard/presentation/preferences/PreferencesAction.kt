package com.jesushz.spendless.dashboard.presentation.preferences

import com.jesushz.spendless.dashboard.domain.Currency
import com.jesushz.spendless.dashboard.domain.DecimalSeparator
import com.jesushz.spendless.dashboard.domain.ExpenseFormat
import com.jesushz.spendless.dashboard.domain.ThousandSeparator

sealed interface PreferencesAction {

    data class OnExpenseFormatSelected(val format: ExpenseFormat): PreferencesAction
    data class OnCurrencySelected(val currency: Currency): PreferencesAction
    data class OnDecimalSeparatorSelected(val separator: DecimalSeparator): PreferencesAction
    data class OnThousandSeparatorSelected(val separator: ThousandSeparator): PreferencesAction
    data object OnStartTrackingClick: PreferencesAction
    data object OnNavigateBack: PreferencesAction

}
