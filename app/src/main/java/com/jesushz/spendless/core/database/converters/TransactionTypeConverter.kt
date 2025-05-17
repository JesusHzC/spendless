package com.jesushz.spendless.core.database.converters

import androidx.room.TypeConverter
import com.jesushz.spendless.core.domain.TransactionType

class TransactionTypeConverter {

    @TypeConverter
    fun fromTransactionType(value: TransactionType): String {
        return value.name
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }

}