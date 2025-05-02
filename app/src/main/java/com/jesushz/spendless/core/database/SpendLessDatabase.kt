package com.jesushz.spendless.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jesushz.spendless.core.database.dao.UserDao
import com.jesushz.spendless.core.database.entity.UserEntity

@Database(
    entities = [
        UserEntity::class
    ],
    version = 1,
)
abstract class SpendLessDatabase: RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        const val DATABASE_NAME = "spendless.db"
    }
}
