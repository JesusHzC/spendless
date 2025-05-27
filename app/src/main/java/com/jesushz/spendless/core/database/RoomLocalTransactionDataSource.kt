package com.jesushz.spendless.core.database

import android.database.sqlite.SQLiteException
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

    override fun getLongestTransaction(userId: String): Flow<TransactionEntity?> {
        return transactionDao.getLongestTransaction(userId)
    }

    override fun getPreviousWeekBalance(userId: String): Flow<Double?> {
        return transactionDao.getPreviousWeekBalance(userId)
    }

    override fun getAccountBalance(userId: String): Flow<Double?> {
        return transactionDao.getAccountBalance(userId)
    }

    override fun getAllTransactions(userId: String): Flow<List<TransactionEntity>> {
        return transactionDao.getAllTransactions(userId)
    }

    override fun getLatestTransactions(userId: String): Flow<List<TransactionEntity>> {
        return transactionDao.getLatestTransactions(userId)
    }

    override fun getTodayRepeatTransactions(userId: String): Flow<List<TransactionEntity>> {
        return transactionDao.getTodayRepeatTransactions(userId)
    }

    override suspend fun clearRepeatDateTime(transactionId: String): EmptyDataResult<DataError.Local> {
        return try {
            transactionDao.clearRepeatDateTime(transactionId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteTransactionById(transactionId: String): EmptyDataResult<DataError.Local> {
        return try {
            transactionDao.deleteTransactionById(transactionId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override fun getComingSoonTransactions(userId: String): Flow<List<TransactionEntity>> {
        return transactionDao.getComingSoonTransactions(userId)
    }

}
