package com.jesushz.spendless.core.domain.transactions


data class CombineTransaction(
    val date: String = "",
    val transactions: List<Transaction> = emptyList()
)
