package com.jesushz.spendless.core.util

/**
 * Validate if string is number
 */
fun String.isNumber(): Boolean {
    return this.trim().toDoubleOrNull() != null
}

/**
 * Transform String to Number
 */
fun String.toNumber(): Double {
    return this.trim().toDoubleOrNull() ?: 0.0
}

/**
 * Mask string with asterisks
 */
fun String.mask(): String {
    return if (isNotEmpty()) {
        "*".repeat(length)
    } else {
        ""
    }
}
