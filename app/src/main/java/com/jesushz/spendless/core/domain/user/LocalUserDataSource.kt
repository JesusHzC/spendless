package com.jesushz.spendless.core.domain.user

import com.jesushz.spendless.core.util.DataError
import com.jesushz.spendless.core.util.EmptyDataResult
import com.jesushz.spendless.core.util.Result as ResultCore

interface LocalUserDataSource {

    suspend fun upsertUser(user: User): EmptyDataResult<DataError.Local>
    suspend fun getUserByUsername(username: String): ResultCore<User?, DataError.Local>
    suspend fun checkIfUsernameExists(username: String): ResultCore<Boolean, DataError.Local>

}
