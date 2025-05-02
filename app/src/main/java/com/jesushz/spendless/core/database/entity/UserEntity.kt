@file:OptIn(ExperimentalUuidApi::class)

package com.jesushz.spendless.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    indices = [
        Index(value = ["username"], unique = true)
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = Uuid.random().toString(),
    val username: String,
    val pin: String,
    val createdAt: Long
)
