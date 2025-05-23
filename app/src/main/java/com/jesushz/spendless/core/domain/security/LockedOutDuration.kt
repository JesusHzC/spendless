package com.jesushz.spendless.core.domain.security

enum class LockedOutDuration(val title: String, val millis: Long) {
    FIFTEEN_SECONDS("15 s", 15 * 1000L),
    THIRTY_SECONDS("30 s", 30 * 1000L),
    ONE_MINUTE("1 min", 60 * 1000L),
    FIVE_MINUTES("5 min", 5 * 60 * 1000L)
}
