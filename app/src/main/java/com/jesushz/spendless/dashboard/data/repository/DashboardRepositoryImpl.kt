package com.jesushz.spendless.dashboard.data.repository

import com.jesushz.spendless.core.database.entity.TransactionEntity
import com.jesushz.spendless.core.domain.transactions.LocalTransactionDataSource
import com.jesushz.spendless.core.util.DataError
import com.jesushz.spendless.core.util.EmptyDataResult
import com.jesushz.spendless.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow

class DashboardRepositoryImpl(
    private val localTransactionDataSource: LocalTransactionDataSource
): DashboardRepository {

    override suspend fun upsertTransaction(transactionEntity: TransactionEntity): EmptyDataResult<DataError.Local> {
        return localTransactionDataSource.upsertTransaction(transactionEntity)
    }

    override fun getTodayTransactions(userId: String): Flow<List<TransactionEntity>> {
        return localTransactionDataSource.getTodayTransactions(userId)
    }

    override fun getYesterdayTransactions(userId: String): Flow<List<TransactionEntity>> {
        return localTransactionDataSource.getYesterdayTransactions(userId)
    }

    override suspend fun getLongestTransaction(userId: String): TransactionEntity? {
        return localTransactionDataSource.getLongestTransaction(userId)
    }

    override suspend fun getPreviousWeekBalance(userId: String): Double? {
        return localTransactionDataSource.getPreviousWeekBalance(userId)
    }

    override suspend fun getAccountBalance(userId: String): Double? {
        return localTransactionDataSource.getAccountBalance(userId)
    }

    override fun getAllTransactions(userId: String): Flow<List<TransactionEntity>> {
        return localTransactionDataSource.getAllTransactions(userId)
    }

}
