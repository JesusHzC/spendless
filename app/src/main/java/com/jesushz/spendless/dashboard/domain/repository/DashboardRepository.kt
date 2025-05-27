package com.jesushz.spendless.dashboard.domain.repository

import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.domain.transactions.TransactionRepeat
import com.jesushz.spendless.core.util.DataError
import com.jesushz.spendless.core.util.EmptyDataResult
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {

    suspend fun upsertTransaction(
        userId: String,
        transaction: Transaction
    ): EmptyDataResult<DataError.Local>
    suspend fun upsertTransactionRepeat(
        userId: String,
        transaction: TransactionRepeat
    ): EmptyDataResult<DataError.Local>
    fun getLongestTransaction(userId: String): Flow<Transaction?>
    fun getPreviousWeekBalance(userId: String): Flow<Double?>
    fun getAccountBalance(userId: String): Flow<Double?>
    fun getAllTransactions(userId: String): Flow<List<Transaction>>
    fun getLatestTransactions(userId: String): Flow<List<Transaction>>
    fun getTodayRepeatTransactions(userId: String): Flow<List<TransactionRepeat>>
    suspend fun clearRepeatDateTime(transactionId: String): EmptyDataResult<DataError.Local>
    suspend fun deleteTransactionById(transactionId: String): EmptyDataResult<DataError.Local>

}
