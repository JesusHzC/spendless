package com.jesushz.spendless.core.domain.security

enum class LockedOutDuration(val title: String) {
    FIFTEEN_SECONDS("15 s"),
    THIRTY_SECONDS("30 s"),
    ONE_MINUTE("1 min"),
    FIVE_MINUTES("5 min"),
}