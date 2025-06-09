package com.jesushz.spendless.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jesushz.spendless.core.database.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    // Upsert transaction
    @Upsert
    suspend fun upsert(transaction: TransactionEntity)

    // Get the transaction with the highest amount
    @Query(
        """
            SELECT * FROM TransactionEntity 
            WHERE userId = :userId
            AND transactionType = 'EXPENSE'
            ORDER BY amount DESC 
            LIMIT 1
        """
    )
    fun getLongestTransaction(userId: String): Flow<TransactionEntity?>

    // Get balance of previous week (Income - Expenses)
    @Query(
        """
            SELECT 
            SUM(CASE WHEN transactionType = 'INCOME' THEN amount ELSE -amount END)
            FROM TransactionEntity
            WHERE dateTime >= datetime('now', '-7 days', 'start of day') 
            AND dateTime < datetime('now', 'start of day')
            AND userId = :userId
        """
    )
    fun getPreviousWeekBalance(userId: String): Flow<Double?>

    // Get current account balance (Income - Expenses)
    @Query(
        """
            SELECT
            SUM(CASE WHEN transactionType = 'INCOME' THEN amount ELSE -amount END)
            FROM TransactionEntity
            WHERE userId = :userId
        """
    )
    fun getAccountBalance(userId: String): Flow<Double?>

    // Get all transactions
    @Query(
        """
            SELECT * FROM TransactionEntity 
            WHERE userId = :userId
            ORDER BY dateTime DESC
        """
    )
    fun getAllTransactions(userId: String): Flow<List<TransactionEntity>>

    // Get the latest transaction (by most recent dateTime)
    @Query(
        """
            SELECT * FROM TransactionEntity 
            WHERE userId = :userId 
            ORDER BY dateTime DESC 
            LIMIT 10
        """
    )
    fun getLatestTransactions(userId: String): Flow<List<TransactionEntity>>

    // Delete transaction by ID
    @Query(
        """
            DELETE FROM TransactionEntity 
            WHERE id = :transactionId
        """
    )
    suspend fun deleteTransactionById(transactionId: String)

    // Export Transactions
    // Get last three months transactions
    @Query(
        """
            SELECT * FROM TransactionEntity 
            WHERE userId = :userId 
            AND dateTime >= datetime('now', '-3 months', 'start of day') 
            ORDER BY dateTime DESC
        """
    )
    fun getLastThreeMonthsTransactions(userId: String): List<TransactionEntity>

    // Get last month transactions
    @Query(
        """
            SELECT * FROM TransactionEntity 
            WHERE userId = :userId 
            AND dateTime >= datetime('now', '-1 month', 'start of day') 
            ORDER BY dateTime DESC
        """
    )
    fun getLastMonthTransactions(userId: String): List<TransactionEntity>

    // Get current month transactions
    @Query(
        """
            SELECT * FROM TransactionEntity 
            WHERE userId = :userId 
            AND dateTime >= datetime('now', 'start of month') 
            ORDER BY dateTime DESC
        """
    )
    fun getCurrentMonthTransactions(userId: String): List<TransactionEntity>

    // Get transactions by custom date range
    @Query(
        """
            SELECT * FROM TransactionEntity 
            WHERE userId = :userId 
            AND dateTime >= :startDate 
            AND dateTime <= :endDate 
            ORDER BY dateTime DESC
        """
    )
    fun getTransactionsByCustomDateRange(
        userId: String,
        startDate: String,
        endDate: String
    ): List<TransactionEntity>

}
