package com.jesushz.spendless.dashboard.domain

enum class Category(
    val title: String,
    val icon: String
) {
    ENTERTAINMENT("Entertainment", "\uD83D\uDCBB"),
    CLOTHING_AND_ACCESSORIES("Clothing & Accessories", "\uD83D\uDC54"),
    EDUCATION("Education", "\uD83C\uDF93"),
    FOOD_AND_GROCERIES("Food & Groceries", "\uD83C\uDF55"),
    HEALTH_AND_WELLNESS("Health & Wellness", "❤\uFE0F"),
    OTHER("Other", "⚙\uFE0F")
}