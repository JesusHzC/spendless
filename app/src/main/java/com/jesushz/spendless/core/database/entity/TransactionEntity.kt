@file:OptIn(ExperimentalUuidApi::class)

package com.jesushz.spendless.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.jesushz.spendless.core.domain.Category
import com.jesushz.spendless.core.domain.Repeat
import com.jesushz.spendless.core.domain.TransactionType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
data class TransactionEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = Uuid.random().toString(),
    val userId: String,
    val category: Category? = null,
    val amount: Double,
    val receiver: String,
    val note: String,
    val dateTime: String,
    val repeat: Repeat,
    val transactionType: TransactionType
)
