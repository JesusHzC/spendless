@file:OptIn(ExperimentalUuidApi::class)

package com.jesushz.spendless.core.domain.transactions

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Transaction(
    val id: String = Uuid.random().toString(),
    val transactionType: TransactionType,
    val category: Category? = null,
    val amount: Double,
    val receiver: String,
    val note: String,
    val date: String,
    val repeat: Repeat
)
