package com.jesushz.spendless.core.database.mappers

import com.jesushz.spendless.core.database.entity.UserEntity
import com.jesushz.spendless.core.domain.user.User

fun UserEntity.toUser(): User {
    return User(
        username = username,
        pin = pin
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        username = username,
        pin = pin,
        createdAt = System.currentTimeMillis()
    )
}
