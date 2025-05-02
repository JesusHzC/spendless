package com.jesushz.spendless.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jesushz.spendless.core.database.entity.UserEntity

@Dao
interface UserDao {

    @Upsert
    suspend fun upsertUser(user: UserEntity)

    @Query("SELECT * FROM userentity WHERE username = :username")
    suspend fun findUserByUsername(username: String): UserEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM userentity WHERE username = :username)")
    suspend fun checkIfUsernameExists(username: String): Boolean

}
