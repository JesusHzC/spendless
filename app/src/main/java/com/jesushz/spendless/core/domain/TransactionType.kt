package com.jesushz.spendless.core.domain

import androidx.annotation.DrawableRes
import com.jesushz.spendless.R

enum class TransactionType(
    val title: String,
    @DrawableRes val icon: Int
) {
    EXPENSE("Expense", R.drawable.ic_expense),
    INCOME("Income", R.drawable.ic_income)
}
