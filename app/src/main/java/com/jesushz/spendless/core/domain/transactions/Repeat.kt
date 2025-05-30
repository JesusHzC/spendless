package com.jesushz.spendless.core.domain.transactions

enum class Repeat(val title: String) {
    NOT_REPEAT("Does not repeat"),
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly")
}
