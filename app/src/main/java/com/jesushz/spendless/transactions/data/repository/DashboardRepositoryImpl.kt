package com.jesushz.spendless.transactions.data.repository

import com.jesushz.spendless.core.database.mappers.toTransaction
import com.jesushz.spendless.core.database.mappers.toTransactionEntity
import com.jesushz.spendless.core.database.mappers.toTransactionPending
import com.jesushz.spendless.core.database.mappers.toTransactionPendingEntity
import com.jesushz.spendless.core.domain.transactions.LocalTransactionDataSource
import com.jesushz.spendless.core.domain.transactions.Repeat
import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.domain.transactions.TransactionPending
import com.jesushz.spendless.core.util.DataError
import com.jesushz.spendless.core.util.EmptyDataResult
import com.jesushz.spendless.transactions.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DashboardRepositoryImpl(
    private val localTransactionDataSource: LocalTransactionDataSource
): DashboardRepository {

    override suspend fun upsertTransaction(
        userId: String,
        transaction: Transaction
    ): EmptyDataResult<DataError.Local> {
        val entity = transaction.toTransactionEntity(userId)

        deleteTransactionPendingByParentId(transaction.id)

        val result = localTransactionDataSource.upsertTransaction(entity)
        if (transaction.repeat != Repeat.NOT_REPEAT) {
            return upsertTransactionPending(
                userId = userId,
                transaction = transaction
            )
        }

        return result
    }

    override suspend fun upsertTransaction(
        userId: String,
        transactionPending: TransactionPending
    ): EmptyDataResult<DataError.Local> {
        val transaction = transactionPending.toTransaction(isUpsert = true)
        return upsertTransaction(userId, transaction)
    }

    override fun getLongestTransaction(userId: String): Flow<Transaction?> {
        return localTransactionDataSource
            .getLongestTransaction(userId)
            .map { entity ->
                entity?.toTransaction()
            }
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
                list.map { it.toTransaction() }
            }
    }

    override fun getLatestTransactions(userId: String): Flow<List<Transaction>> {
        return localTransactionDataSource
            .getLatestTransactions(userId)
            .map { list ->
                list.map { it.toTransaction() }
            }
    }

    override suspend fun deleteTransactionById(transactionId: String) {
        return localTransactionDataSource.deleteTransactionById(transactionId)
    }

    // Pending Transactions
    override suspend fun upsertTransactionPending(
        userId: String,
        transaction: Transaction
    ): EmptyDataResult<DataError.Local> {
        val entity = transaction
            .toTransactionPendingEntity(userId)

        return localTransactionDataSource.upsertTransactionPending(entity)
    }

    override fun getTodayTransactionsPending(userId: String): Flow<List<TransactionPending>> {
        return localTransactionDataSource
            .getTodayRepeatTransactions(userId)
            .map { list ->
                list.map { it.toTransactionPending() }
            }
    }

    override fun getAllPendingTransactionsPending(userId: String): Flow<List<TransactionPending>> {
        return localTransactionDataSource
            .getAllPendingTransactionsPending(userId)
            .map { list ->
                list.map { it.toTransactionPending() }
            }
    }

    override suspend fun deleteTransactionPendingById(transactionPendingId: String) {
        return localTransactionDataSource.deleteTransactionPendingById(transactionPendingId)
    }

    override suspend fun deleteTransactionPendingByParentId(parentId: String) {
        return localTransactionDataSource.deleteTransactionPendingByParentId(parentId)
    }

    override suspend fun getLastThreeMonthsTransactions(userId: String): List<Transaction> {
        return localTransactionDataSource
            .getLastThreeMonthsTransactions(userId)
            .map { it.toTransaction() }
    }

    override suspend fun getLastMonthTransactions(userId: String): List<Transaction> {
        return localTransactionDataSource
            .getLastMonthTransactions(userId)
            .map { it.toTransaction() }
    }

    override suspend fun getCurrentMonthTransactions(userId: String): List<Transaction> {
        return localTransactionDataSource
            .getCurrentMonthTransactions(userId)
            .map { it.toTransaction() }
    }

    override fun getTransactionsByCustomDateRange(
        userId: String,
        startDate: String,
        endDate: String
    ): List<Transaction> {
        return localTransactionDataSource
            .getTransactionsByCustomDateRange(userId, startDate, endDate)
            .map { it.toTransaction() }
    }

}
