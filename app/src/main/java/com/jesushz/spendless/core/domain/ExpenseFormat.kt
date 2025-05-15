package com.jesushz.spendless.core.domain

enum class ExpenseFormat(val value: String) {
    NEGATIVE("-$10"),
    POSITIVE("($10)"),
}
