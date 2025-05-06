package com.jesushz.spendless.dashboard.domain

enum class ExpenseFormat(val value: String) {
    NEGATIVE("-$10"),
    POSITIVE("($10)"),
}
