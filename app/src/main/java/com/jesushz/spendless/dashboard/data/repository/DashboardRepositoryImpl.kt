package com.jesushz.spendless.dashboard.data.repository

import com.jesushz.spendless.core.database.mappers.toTransaction
import com.jesushz.spendless.core.database.mappers.toTransactionEntity
import com.jesushz.spendless.core.domain.transactions.LocalTransactionDataSource
import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.util.DataError
import com.jesushz.spendless.core.util.EmptyDataResult
import com.jesushz.spendless.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class DashboardRepositoryImpl(
    private val localTransactionDataSource: LocalTransactionDataSource
): DashboardRepository {

    override suspend fun upsertTransaction(
        userId: String,
        transaction: Transaction
    ): EmptyDataResult<DataError.Local> {
        val entity = transaction
            .toTransactionEntity()
            .copy(
                userId = userId
            )
        return localTransactionDataSource.upsertTransaction(entity)
    }

    override fun getTodayTransactions(userId: String): Flow<List<Transaction>> {
        return localTransactionDataSource
            .getTodayTransactions(userId)
            .map { list ->
                list.map {
                    it.toTransaction()
                }
            }
    }

    override fun getYesterdayTransactions(userId: String): Flow<List<Transaction>> {
        return localTransactionDataSource
            .getYesterdayTransactions(userId)
            .map { list ->
                list.map {
                    it.toTransaction()
                }
            }
    }

    override fun getLongestTransaction(userId: String): Flow<Transaction?> {
        return localTransactionDataSource
            .getLongestTransaction(userId)
            .map { it?.toTransaction() }
    }

    override suspend fun getPreviousWeekBalance(userId: String): Double? {
        return localTransactionDataSource.getPreviousWeekBalance(userId)
    }

    override fun getAccountBalance(userId: String): Flow<Double?> {
        return localTransactionDataSource.getAccountBalance(userId)
    }

    override fun getAllTransactions(userId: String): Flow<List<Transaction>> {
        return localTransactionDataSource
            .getAllTransactions(userId)
            .map { list ->
                list.map {
                    it.toTransaction()
                }
            }
    }

    override fun getLatestTransaction(userId: String): Flow<Transaction?> {
        return localTransactionDataSource
            .getLatestTransaction(userId)
            .map { it?.toTransaction() }
    }

}
