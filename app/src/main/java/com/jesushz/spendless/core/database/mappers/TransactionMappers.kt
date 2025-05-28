package com.jesushz.spendless.core.database.mappers

import com.jesushz.spendless.core.database.entity.TransactionEntity
import com.jesushz.spendless.core.domain.transactions.Repeat
import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.domain.transactions.TransactionRepeat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Transaction.toTransactionEntity(): TransactionEntity {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val baseDate = LocalDateTime.parse(date, formatter)

    val dateRepeat = when (repeat) {
        Repeat.NOT_REPEAT -> null
        Repeat.DAILY -> baseDate.plusDays(1).format(formatter)
        Repeat.WEEKLY -> baseDate.plusWeeks(1).format(formatter)
        Repeat.MONTHLY -> baseDate.plusMonths(1).format(formatter)
        Repeat.YEARLY -> baseDate.plusYears(1).format(formatter)
    }
    return TransactionEntity(
        id = id,
        transactionType = transactionType,
        category = category,
        amount = amount,
        receiver = receiver,
        note = note,
        dateTime = date,
        repeat = repeat,
        repeatDateTime = dateRepeat
    )
}

fun TransactionEntity.toTransaction(isComingSoon: Boolean = false): Transaction {
    return Transaction(
        id = id,
        transactionType = transactionType,
        category = category,
        amount = amount,
        receiver = receiver,
        note = note,
        date = if (isComingSoon) repeatDateTime ?: "" else dateTime,
        repeat = repeat
    )
}

fun TransactionEntity.toTransactionRepeat(): TransactionRepeat {
    return TransactionRepeat(
        transactionType = transactionType,
        category = category,
        amount = amount,
        receiver = receiver,
        note = note,
        date = repeatDateTime ?: "",
        repeat = repeat,
        oldTransactionId = id
    )
}

fun TransactionRepeat.toTransactionEntity(): TransactionEntity {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val baseDate = LocalDateTime.parse(date, formatter)

    val dateRepeat = when (repeat) {
        Repeat.NOT_REPEAT -> null
        Repeat.DAILY -> baseDate.plusDays(1).format(formatter)
        Repeat.WEEKLY -> baseDate.plusWeeks(1).format(formatter)
        Repeat.MONTHLY -> baseDate.plusMonths(1).format(formatter)
        Repeat.YEARLY -> baseDate.plusYears(1).format(formatter)
    }
    return TransactionEntity(
        id = id,
        transactionType = transactionType,
        category = category,
        amount = amount,
        receiver = receiver,
        note = note,
        dateTime = date,
        repeat = repeat,
        repeatDateTime = dateRepeat
    )
}
