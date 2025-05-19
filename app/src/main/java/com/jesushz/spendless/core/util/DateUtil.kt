package com.jesushz.spendless.core.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 * Get current day of Month
 */
fun getCurrentDayOfMonth(): String {
    return LocalDate.now().dayOfMonth.toString()
}

/**
 * Get name of current week
 */
fun getCurrentWeekName(): String {
    val dayOfWeek = LocalDate.now().dayOfWeek
    return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
}

/**
 * Get current month name
 */
fun getCurrentMonthName(): String {
    val month = LocalDate.now().month
    return month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
}

/**
 * Get current day of Month with suffix
 */
fun getCurrentDayOfMonthWithSuffix(): String {
    val day = LocalDate.now().dayOfMonth
    val suffix = when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }
    return "$day$suffix"
}

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
