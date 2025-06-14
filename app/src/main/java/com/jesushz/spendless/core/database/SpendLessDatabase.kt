package com.jesushz.spendless.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jesushz.spendless.core.database.converters.CategoryTypeConverter
import com.jesushz.spendless.core.database.converters.TransactionTypeConverter
import com.jesushz.spendless.core.database.dao.TransactionDao
import com.jesushz.spendless.core.database.dao.TransactionPendingDao
import com.jesushz.spendless.core.database.dao.UserDao
import com.jesushz.spendless.core.database.entity.TransactionEntity
import com.jesushz.spendless.core.database.entity.TransactionPendingEntity
import com.jesushz.spendless.core.database.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TransactionEntity::class,
        TransactionPendingEntity::class
    ],
    version = 5,
)
@TypeConverters(
    TransactionTypeConverter::class,
    CategoryTypeConverter::class
)
abstract class SpendLessDatabase: RoomDatabase() {

    abstract val userDao: UserDao
    abstract val transactionDao: TransactionDao
    abstract val transactionPendingDao: TransactionPendingDao

    companion object {
        const val DATABASE_NAME = "spendless.db"
    }
}
