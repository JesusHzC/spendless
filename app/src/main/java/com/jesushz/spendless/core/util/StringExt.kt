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
