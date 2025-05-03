package com.jesushz.spendless.auth.data.repository

import com.jesushz.spendless.auth.domain.repository.AuthRepository
import com.jesushz.spendless.core.domain.user.LocalUserDataSource
import com.jesushz.spendless.core.domain.user.User
import com.jesushz.spendless.core.util.DataError
import com.jesushz.spendless.core.util.EmptyDataResult
import com.jesushz.spendless.core.util.Result

class AuthRepositoryImpl(
    private val localUserDataSource: LocalUserDataSource
): AuthRepository {

    override suspend fun login(username: String): Result<User?, DataError.Local> {
        return localUserDataSource.getUserByUsername(username)
    }

    override suspend fun checkIfUsernameExists(username: String): Result<Boolean, DataError.Local> {
        return localUserDataSource.checkIfUsernameExists(username)
    }

    override suspend fun register(
        user: User
    ): EmptyDataResult<DataError.Local> {
        return localUserDataSource.upsertUser(user)
    }

}
