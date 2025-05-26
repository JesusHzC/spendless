package com.jesushz.spendless.dashboard.presentation.create_transaction

import com.jesushz.spendless.core.domain.transactions.Category
import com.jesushz.spendless.core.domain.transactions.Currency
import com.jesushz.spendless.core.domain.transactions.DecimalSeparator
import com.jesushz.spendless.core.domain.transactions.ExpenseFormat
import com.jesushz.spendless.core.domain.transactions.Repeat
import com.jesushz.spendless.core.domain.transactions.ThousandSeparator
import com.jesushz.spendless.core.domain.transactions.TransactionType
import com.jesushz.spendless.core.util.getDateFormat

data class CreateTransactionState(
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val receiver: String = "",
    val amount: String = "",
    val note: String = "",
    val categorySelected: Category = Category.OTHER,
    val repeatSelected: Repeat = Repeat.NOT_REPEAT,
    val userId: String = "",
    val dateSelected: String = getDateFormat(),
    val currency: Currency = Currency.MEXICAN_PESO,
    val expenseFormat: ExpenseFormat = ExpenseFormat.NEGATIVE,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.POINT,
    val thousandSeparator: ThousandSeparator = ThousandSeparator.COMMA,
)
