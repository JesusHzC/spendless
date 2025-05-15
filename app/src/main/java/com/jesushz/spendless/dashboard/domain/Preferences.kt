package com.jesushz.spendless.dashboard.domain

import com.jesushz.spendless.core.domain.Currency
import com.jesushz.spendless.core.domain.DecimalSeparator
import com.jesushz.spendless.core.domain.ExpenseFormat
import com.jesushz.spendless.core.domain.ThousandSeparator
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class Preferences(
    private val applicationScope: CoroutineScope,
    private val dataStoreManager: DataStoreManager
) {
    
    private var _expenseFormat: ExpenseFormat? = null
    val expenseFormat: ExpenseFormat
        get() = _expenseFormat ?: error("Preferences not initialized")

    private var _currency: Currency? = null
    val currency: Currency
        get() = _currency ?: error("Preferences not initialized")

    private var _decimalSeparator: DecimalSeparator? = null
    val decimalSeparator: DecimalSeparator
        get() = _decimalSeparator ?: error("Preferences not initialized")

    private var _thousandSeparator: ThousandSeparator? = null
    val thousandSeparator: ThousandSeparator
        get() = _thousandSeparator ?: error("Preferences not initialized")

    suspend fun initialize() {
        applicationScope.launch {
            _expenseFormat = dataStoreManager.getExpenseFormat()
            _currency = dataStoreManager.getCurrency()
            _decimalSeparator = dataStoreManager.getDecimalSeparator()
            _thousandSeparator = dataStoreManager.getThousandSeparator()
        }.join()
    }

    fun updateExpenseFormat(format: ExpenseFormat) {
        _expenseFormat = format
    }

    fun updateCurrency(currency: Currency) {
        _currency = currency
    }

    fun updateDecimalSeparator(separator: DecimalSeparator) {
        _decimalSeparator = separator
    }

    fun updateThousandSeparator(separator: ThousandSeparator) {
        _thousandSeparator = separator
    }

    suspend fun savePreferences() {
        applicationScope.launch {
            dataStoreManager.saveExpenseFormat(expenseFormat)
            dataStoreManager.saveCurrency(currency)
            dataStoreManager.saveDecimalSeparator(decimalSeparator)
            dataStoreManager.saveThousandSeparator(thousandSeparator)
        }.join()
    }

    fun formatAmount(amount: Number): String {
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
            decimalSeparator = decimalSeparatorChar
            groupingSeparator = thousandSeparatorChar
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

        val expense =  when (expenseFormat) {
            ExpenseFormat.NEGATIVE -> "-$currencySymbol$formatted"
            ExpenseFormat.POSITIVE -> "($currencySymbol$formatted)"
        }

        Timber.d("formatAmount: $expense")

        return expense
    }

}
