package com.jesushz.spendless.core.util

import com.jesushz.spendless.core.domain.transactions.Currency
import com.jesushz.spendless.core.domain.transactions.DecimalSeparator
import com.jesushz.spendless.core.domain.transactions.ExpenseFormat
import com.jesushz.spendless.core.domain.transactions.ThousandSeparator
import timber.log.Timber
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object PrefsUtil {

    fun formatAmount(
        amount: Number,
        currency: Currency,
        expenseFormat: ExpenseFormat,
        decimalSeparator: DecimalSeparator,
        thousandSeparator: ThousandSeparator
    ): String {
        val decimalSeparatorChar = when (decimalSeparator) {
            DecimalSeparator.POINT -> '.'
            DecimalSeparator.COMMA -> ','
        }

        val thousandSeparatorChar = when (thousandSeparator) {
            ThousandSeparator.POINT -> '.'
            ThousandSeparator.COMMA -> ','
            ThousandSeparator.SPACE -> ' '
        }

        val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
            this.decimalSeparator = decimalSeparatorChar
            this.groupingSeparator = thousandSeparatorChar
        }

        val formatter = DecimalFormat("#,##0.00", symbols)
        val formatted = formatter.format(amount.toDouble())

        val currencySymbol = when (currency) {
            Currency.MEXICAN_PESO -> "$"
            Currency.DOLLAR -> "$"
            Currency.EURO -> "€"
            Currency.BRITISH_POUND -> "£"
            Currency.JAPANESE_YEN -> "¥"
            Currency.SWISS_FRANC -> "₣"
        }

        val expense = when (expenseFormat) {
            ExpenseFormat.NEGATIVE -> "-$currencySymbol$formatted"
            ExpenseFormat.POSITIVE -> "($currencySymbol$formatted)"
        }

        Timber.d("formatAmount: $expense")
        return expense
    }

}