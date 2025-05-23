package com.jesushz.spendless.core.domain.security

enum class SessionDuration(val title: String, val millis: Long) {
    FIVE_MINUTES("5 min", 5 * 60 * 1000L),
    FIFTEEN_MINUTES("15 min", 15 * 60 * 1000L),
    THIRTY_MINUTES("30 min", 30 * 60 * 1000L),
    ONE_HOUR("1 hour", 60 * 60 * 1000L)
}
