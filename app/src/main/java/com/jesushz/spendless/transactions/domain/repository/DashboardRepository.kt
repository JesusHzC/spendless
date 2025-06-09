package com.jesushz.spendless.transactions.domain.repository

import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.domain.transactions.TransactionPending
import com.jesushz.spendless.core.util.DataError
import com.jesushz.spendless.core.util.EmptyDataResult
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {

    suspend fun upsertTransaction(
        userId: String,
        transaction: Transaction
    ): EmptyDataResult<DataError.Local>
    suspend fun upsertTransaction(
        userId: String,
        transactionPending: TransactionPending
    ): EmptyDataResult<DataError.Local>
    fun getLongestTransaction(userId: String): Flow<Transaction?>
    fun getPreviousWeekBalance(userId: String): Flow<Double?>
    fun getAccountBalance(userId: String): Flow<Double?>
    fun getAllTransactions(userId: String): Flow<List<Transaction>>
    fun getLatestTransactions(userId: String): Flow<List<Transaction>>
    suspend fun deleteTransactionById(transactionId: String)

    suspend fun upsertTransactionPending(
        userId: String,
        transaction: Transaction
    ): EmptyDataResult<DataError.Local>
    fun getTodayTransactionsPending(userId: String): Flow<List<TransactionPending>>
    fun getAllPendingTransactionsPending(userId: String): Flow<List<TransactionPending>>
    suspend fun deleteTransactionPendingById(transactionPendingId: String)
    suspend fun deleteTransactionPendingByParentId(parentId: String)

    suspend fun getLastThreeMonthsTransactions(userId: String): List<Transaction>
    suspend fun getLastMonthTransactions(userId: String): List<Transaction>
    suspend fun getCurrentMonthTransactions(userId: String): List<Transaction>
    fun getTransactionsByCustomDateRange(
        userId: String,
        startDate: String,
        endDate: String
    ): List<Transaction>
}
