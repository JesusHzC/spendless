package com.jesushz.spendless.core.database.mappers

import com.jesushz.spendless.core.database.entity.TransactionEntity
import com.jesushz.spendless.core.database.entity.TransactionPendingEntity
import com.jesushz.spendless.core.domain.transactions.Repeat
import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.domain.transactions.TransactionPending
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun Transaction.toTransactionEntity(userId: String): TransactionEntity {
    return TransactionEntity(
        id = id,
        transactionType = transactionType,
        category = category,
        amount = amount,
        receiver = receiver,
        note = note,
        dateTime = date,
        repeat = repeat,
        userId = userId
    )
}

fun TransactionEntity.toTransaction(): Transaction {
    return Transaction(
        id = id,
        transactionType = transactionType,
        category = category,
        amount = amount,
        receiver = receiver,
        note = note,
        date = dateTime,
        repeat = repeat
    )
}

fun Transaction.toTransactionPendingEntity(userId: String): TransactionPendingEntity {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val baseDate = LocalDateTime.parse(date, formatter)

    val dateRepeat = when (repeat) {
        Repeat.NOT_REPEAT -> ""
        Repeat.DAILY -> baseDate.plusDays(1).format(formatter)
        Repeat.WEEKLY -> baseDate.plusWeeks(1).format(formatter)
        Repeat.MONTHLY -> baseDate.plusMonths(1).format(formatter)
        Repeat.YEARLY -> baseDate.plusYears(1).format(formatter)
    }

    return TransactionPendingEntity(
        userId = userId,
        transactionType = transactionType,
        category = category,
        amount = amount,
        receiver = receiver,
        note = note,
        dateTime = dateRepeat,
        repeat = repeat,
        transactionParentId = id
    )
}

fun TransactionPendingEntity.toTransactionPending(): TransactionPending {
    return TransactionPending(
        id = id,
        transactionType = transactionType,
        category = category,
        amount = amount,
        receiver = receiver,
        note = note,
        date = dateTime,
        repeat = repeat,
        transactionParentId = transactionParentId
    )
}

@OptIn(ExperimentalUuidApi::class)
fun TransactionPending.toTransaction(isUpsert: Boolean = false): Transaction {
    return Transaction(
        id = if (isUpsert) Uuid.random().toString() else id,
        transactionType = transactionType,
        category = category,
        amount = amount,
        receiver = receiver,
        note = note,
        date = date,
        repeat = repeat
    )
}
