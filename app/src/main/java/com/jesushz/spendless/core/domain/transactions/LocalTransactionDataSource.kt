package com.jesushz.spendless.core.domain.transactions

import com.jesushz.spendless.core.database.entity.TransactionEntity
import com.jesushz.spendless.core.util.DataError
import com.jesushz.spendless.core.util.EmptyDataResult
import kotlinx.coroutines.flow.Flow

interface LocalTransactionDataSource {

    suspend fun upsertTransaction(transactionEntity: TransactionEntity): EmptyDataResult<DataError.Local>
    fun getTodayTransactions(userId: String): Flow<List<TransactionEntity>>
    fun getYesterdayTransactions(userId: String): Flow<List<TransactionEntity>>
    fun getLongestTransaction(userId: String): Flow<TransactionEntity?>
    suspend fun getPreviousWeekBalance(userId: String): Double?
    fun getAccountBalance(userId: String): Flow<Double?>
    fun getAllTransactions(userId: String): Flow<List<TransactionEntity>>
    fun getLatestTransaction(userId: String): Flow<TransactionEntity?>

}
