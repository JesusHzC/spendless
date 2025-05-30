package com.jesushz.spendless.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jesushz.spendless.core.database.entity.TransactionPendingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionPendingDao {

    @Upsert
    suspend fun upsert(transaction: TransactionPendingEntity)

    // Get all pending transactions for a user
    @Query(
        """
            SELECT * FROM TransactionPendingEntity 
            WHERE userId = :userId
            ORDER BY dateTime ASC
        """
    )
    fun getAllPendingTransactions(userId: String): Flow<List<TransactionPendingEntity>>

    // Get transactions where dateTime is today
    @Query(
        """
            SELECT * FROM TransactionPendingEntity 
            WHERE dateTime IS NOT NULL 
            AND date(dateTime) <= date('now') 
            AND userId = :userId
        """
    )
    fun getTodayRepeatTransactions(userId: String): Flow<List<TransactionPendingEntity>>

    // Delete a pending transaction by its ID
    @Query(
        """
            DELETE FROM TransactionPendingEntity WHERE id = :transactionId
        """
    )
    suspend fun deletePendingTransaction(transactionId: String)

    // Delete all pending transactions for transactionParentId
    @Query(
        """
            DELETE FROM TransactionPendingEntity WHERE transactionParentId = :transactionParentId
        """
    )
    suspend fun deletePendingTransactionsByParentId(transactionParentId: String)

}
