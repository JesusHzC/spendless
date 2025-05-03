package com.jesushz.spendless.auth.domain.repository

import com.jesushz.spendless.core.domain.user.User
import com.jesushz.spendless.core.util.DataError
import com.jesushz.spendless.core.util.EmptyDataResult
import com.jesushz.spendless.core.util.Result

interface AuthRepository {

    suspend fun login(username: String): Result<User?, DataError.Local>
    suspend fun checkIfUsernameExists(username: String): Result<Boolean, DataError.Local>
    suspend fun register(user: User): EmptyDataResult<DataError.Local>

}
