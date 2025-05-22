package com.jesushz.spendless.dashboard.domain.repository

import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.domain.transactions.TransactionRepeat
import com.jesushz.spendless.core.util.DataError
import com.jesushz.spendless.core.util.EmptyDataResult
import com.jesushz.spendless.core.util.Result
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
    fun getTodayTransactions(userId: String): Flow<List<Transaction>>
    fun getYesterdayTransactions(userId: String): Flow<List<Transaction>>
    fun getLongestTransaction(userId: String): Flow<Transaction?>
    suspend fun getPreviousWeekBalance(userId: String): Double?
    fun getAccountBalance(userId: String): Flow<Double?>
    fun getAllTransactions(userId: String): Flow<List<Transaction>>
    fun getLatestTransaction(userId: String): Flow<Transaction?>
    suspend fun getTodayRepeatTransactions(userId: String): Result<List<TransactionRepeat>, DataError.Local>
    suspend fun clearRepeatDateTime(transactionId: String): EmptyDataResult<DataError.Local>

}
