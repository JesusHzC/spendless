package com.jesushz.spendless.core.database

import android.database.sqlite.SQLiteFullException
import com.jesushz.spendless.core.database.dao.TransactionDao
import com.jesushz.spendless.core.database.entity.TransactionEntity
import com.jesushz.spendless.core.domain.transactions.LocalTransactionDataSource
import com.jesushz.spendless.core.util.DataError
import com.jesushz.spendless.core.util.EmptyDataResult
import com.jesushz.spendless.core.util.Result
import kotlinx.coroutines.flow.Flow
import timber.log.Timber


class RoomLocalTransactionDataSource(
    private val transactionDao: TransactionDao
): LocalTransactionDataSource {

    override suspend fun upsertTransaction(transactionEntity: TransactionEntity): EmptyDataResult<DataError.Local> {
        return try {
            transactionDao.upsert(transactionEntity)
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Timber.e(e)
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override fun getTodayTransactions(userId: String): Flow<List<TransactionEntity>> {
        return transactionDao.getTodayTransactions(userId)
    }

    override fun getYesterdayTransactions(userId: String): Flow<List<TransactionEntity>> {
        return transactionDao.getYesterdayTransactions(userId)
    }

    override suspend fun getLongestTransaction(userId: String): TransactionEntity? {
        return transactionDao.getLongestTransaction(userId)
    }

    override suspend fun getPreviousWeekBalance(userId: String): Double? {
        return transactionDao.getPreviousWeekBalance(userId)
    }

    override suspend fun getAccountBalance(userId: String): Double? {
        return transactionDao.getAccountBalance(userId)
    }

    override fun getAllTransactions(userId: String): Flow<List<TransactionEntity>> {
        return transactionDao.getAllTransactions(userId)
    }

}
