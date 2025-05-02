package com.jesushz.spendless.core.database

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteFullException
import com.jesushz.spendless.core.database.dao.UserDao
import com.jesushz.spendless.core.database.mappers.toUser
import com.jesushz.spendless.core.database.mappers.toUserEntity
import com.jesushz.spendless.core.domain.user.LocalUserDataSource
import com.jesushz.spendless.core.domain.user.User
import com.jesushz.spendless.core.util.DataError
import com.jesushz.spendless.core.util.EmptyDataResult
import com.jesushz.spendless.core.util.Result

class RoomLocalUserDataSource(
    private val userDao: UserDao
): LocalUserDataSource {

    override suspend fun upsertUser(user: User): EmptyDataResult<DataError.Local> {
        return try {
            val entity = user.toUserEntity()
            userDao.upsertUser(entity)
            Result.Success(Unit)
        } catch (e: SQLiteConstraintException) {
            Result.Error(DataError.Local.CONSTRAINT_VIOLATION)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getUserByUsername(username: String): Result<User?, DataError.Local> {
        return userDao.findUserByUsername(username)?.let {
            Result.Success(it.toUser())
        } ?: Result.Success(null)
    }

    override suspend fun checkIfUsernameExists(username: String): Result<Boolean, DataError.Local> {
        val exists = userDao.checkIfUsernameExists(username)
        return if (exists) {
            Result.Success(true)
        } else {
            Result.Success(false)
        }
    }

}
