package com.jesushz.spendless.core.domain.transactions

import com.jesushz.spendless.core.database.entity.TransactionEntity
import com.jesushz.spendless.core.database.entity.TransactionPendingEntity
import com.jesushz.spendless.core.util.DataError
import com.jesushz.spendless.core.util.EmptyDataResult
import kotlinx.coroutines.flow.Flow

interface LocalTransactionDataSource {

    suspend fun upsertTransaction(transactionEntity: TransactionEntity): EmptyDataResult<DataError.Local>
    fun getLongestTransaction(userId: String): Flow<TransactionEntity?>
    fun getPreviousWeekBalance(userId: String): Flow<Double?>
    fun getAccountBalance(userId: String): Flow<Double?>
    fun getAllTransactions(userId: String): Flow<List<TransactionEntity>>
    fun getLatestTransactions(userId: String): Flow<List<TransactionEntity>>
    suspend fun deleteTransactionById(transactionId: String)

    // Pending Transactions
    suspend fun upsertTransactionPending(transactionPendingEntity: TransactionPendingEntity): EmptyDataResult<DataError.Local>
    fun getAllPendingTransactionsPending(userId: String): Flow<List<TransactionPendingEntity>>
    fun getTodayRepeatTransactions(userId: String): Flow<List<TransactionPendingEntity>>
    suspend fun deleteTransactionPendingById(transactionPendingId: String)
    suspend fun deleteTransactionPendingByParentId(transactionParentId: String)

}
