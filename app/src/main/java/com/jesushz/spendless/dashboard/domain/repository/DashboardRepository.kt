package com.jesushz.spendless.dashboard.domain.repository

import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.util.DataError
import com.jesushz.spendless.core.util.EmptyDataResult
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {

    suspend fun upsertTransaction(
        userId: String,
        transaction: Transaction
    ): EmptyDataResult<DataError.Local>
    fun getTodayTransactions(userId: String): Flow<List<Transaction>>
    fun getYesterdayTransactions(userId: String): Flow<List<Transaction>>
    fun getLongestTransaction(userId: String): Flow<Transaction?>
    suspend fun getPreviousWeekBalance(userId: String): Double?
    fun getAccountBalance(userId: String): Flow<Double?>
    fun getAllTransactions(userId: String): Flow<List<Transaction>>
    fun getLatestTransaction(userId: String): Flow<Transaction?>

}
