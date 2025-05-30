@file:OptIn(ExperimentalUuidApi::class)

package com.jesushz.spendless.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jesushz.spendless.core.domain.transactions.Category
import com.jesushz.spendless.core.domain.transactions.Repeat
import com.jesushz.spendless.core.domain.transactions.TransactionType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
data class TransactionPendingEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = Uuid.random().toString(),
    val userId: String,
    val category: Category? = null,
    val amount: Double,
    val receiver: String,
    val note: String,
    val repeat: Repeat,
    val transactionType: TransactionType,
    val transactionParentId: String,
    val dateTime: String
)
