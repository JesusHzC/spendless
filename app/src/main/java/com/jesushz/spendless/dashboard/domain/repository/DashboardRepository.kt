package com.jesushz.spendless.dashboard.domain.repository

import com.jesushz.spendless.core.database.entity.TransactionEntity
import com.jesushz.spendless.core.domain.transactions.Category
import com.jesushz.spendless.core.domain.transactions.Repeat
import com.jesushz.spendless.core.domain.transactions.TransactionType
import com.jesushz.spendless.core.util.DataError
import com.jesushz.spendless.core.util.EmptyDataResult
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {

    suspend fun upsertTransaction(
        userId: String,
        category: Category,
        amount: Double,
        receiver: String,
        note: String,
        dateTime: String,
        repeat: Repeat,
        transactionType: TransactionType
    ): EmptyDataResult<DataError.Local>
    fun getTodayTransactions(userId: String): Flow<List<TransactionEntity>>
    fun getYesterdayTransactions(userId: String): Flow<List<TransactionEntity>>
    suspend fun getLongestTransaction(userId: String): TransactionEntity?
    suspend fun getPreviousWeekBalance(userId: String): Double?
    suspend fun getAccountBalance(userId: String): Double?
    fun getAllTransactions(userId: String): Flow<List<TransactionEntity>>

}
