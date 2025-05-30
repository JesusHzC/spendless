package com.jesushz.spendless.core.util

import com.jesushz.spendless.core.domain.transactions.Repeat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Get date time
 *
 * Example: 2025-05-16T17:45:00
 */
fun getDateFormat(): String {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    return LocalDateTime.now().format(formatter)
}

/***
 * Receive getDateFormat 2025-05-16T17:45:00
 *
 * @return Jan 7, 2025
 */
fun formatToReadableDate(input: String): String {
    val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.getDefault())
    val dateTime = LocalDateTime.parse(input, inputFormatter)
    return dateTime.format(outputFormatter)
}

/**
 * Convert formatted date to Millis
 *
 * Example: 2025-05-16T17:45:00
 *
 * @return 1684257900000
 */
fun parseDateToMillis(input: String): Long {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val dateTime = LocalDateTime.parse(input, formatter)
    return dateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
}

/**
 * Get next pending date by repeat and date selected
 */
fun getNextPendingDate(repeat: Repeat, oldDate: String): String? {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val baseDate = LocalDateTime.parse(oldDate, formatter)

    val dateRepeat = when (repeat) {
        Repeat.NOT_REPEAT -> null
        Repeat.DAILY -> baseDate.plusDays(1).format(formatter)
        Repeat.WEEKLY -> baseDate.plusWeeks(1).format(formatter)
        Repeat.MONTHLY -> baseDate.plusMonths(1).format(formatter)
        Repeat.YEARLY -> baseDate.plusYears(1).format(formatter)
    }

    return dateRepeat
}
