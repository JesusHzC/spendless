package com.jesushz.spendless.core.domain.preferences

import com.jesushz.spendless.core.domain.user.User

interface DataStoreManager {

    suspend fun saveUser(user: User)
    suspend fun getUser(): User

}
