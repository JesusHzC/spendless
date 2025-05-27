package com.jesushz.spendless.dashboard.data.repository

import com.jesushz.spendless.core.database.mappers.toTransaction
import com.jesushz.spendless.core.database.mappers.toTransactionEntity
import com.jesushz.spendless.core.database.mappers.toTransactionRepeat
import com.jesushz.spendless.core.domain.transactions.LocalTransactionDataSource
import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.domain.transactions.TransactionRepeat
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

    override suspend fun upsertTransactionRepeat(
        userId: String,
        transaction: TransactionRepeat
    ): EmptyDataResult<DataError.Local> {
        val entity = transaction
            .toTransactionEntity()
            .copy(
                userId = userId
            )
        return localTransactionDataSource.upsertTransaction(entity)
    }

    override fun getLongestTransaction(userId: String): Flow<Transaction?> {
        return localTransactionDataSource
            .getLongestTransaction(userId)
            .map { it?.toTransaction() }
    }

    override fun getPreviousWeekBalance(userId: String): Flow<Double?> {
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

    override fun getLatestTransactions(userId: String): Flow<List<Transaction>> {
        return localTransactionDataSource
            .getLatestTransactions(userId)
            .map { list ->
                list.map {
                    it.toTransaction()
                }
            }
    }

    override fun getTodayRepeatTransactions(userId: String): Flow<List<TransactionRepeat>> {
        return localTransactionDataSource
            .getTodayRepeatTransactions(userId)
            .map { list ->
                list.map {
                    it.toTransactionRepeat()
                }
            }
    }

    override suspend fun clearRepeatDateTime(transactionId: String): EmptyDataResult<DataError.Local> {
        return localTransactionDataSource.clearRepeatDateTime(transactionId)
    }

    override suspend fun deleteTransactionById(transactionId: String): EmptyDataResult<DataError.Local> {
        return localTransactionDataSource.deleteTransactionById(transactionId)
    }

    override fun getComingSoonTransactions(userId: String): Flow<List<Transaction>> {
        return localTransactionDataSource
            .getComingSoonTransactions(userId)
            .map { list ->
                list.map {
                    it.toTransaction()
                }
            }
    }

}
