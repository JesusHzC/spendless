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

    // Get today's transactions
    @Query(
        """
            SELECT * FROM TransactionEntity 
            WHERE date(dateTime) = date('now') AND userId = :userId
            ORDER BY dateTime DESC
        """
    )
    fun getTodayTransactions(userId: String): Flow<List<TransactionEntity>>

    // Get yesterday's transactions
    @Query(
        """
            SELECT * FROM TransactionEntity 
            WHERE date(dateTime) = date('now', '-1 day') AND userId = :userId
            ORDER BY dateTime DESC
        """
    )
    fun getYesterdayTransactions(userId: String): Flow<List<TransactionEntity>>

    // Get the transaction with the highest amount
    @Query(
        """
            SELECT * FROM TransactionEntity 
            WHERE userId = :userId 
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
            WHERE date(dateTime) >= date('now', '-7 days') 
            AND date(dateTime) < date('now')
            AND userId = :userId
        """
    )
    suspend fun getPreviousWeekBalance(userId: String): Double?

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
        LIMIT 1
    """
    )
    fun getLatestTransaction(userId: String): Flow<TransactionEntity?>

}
