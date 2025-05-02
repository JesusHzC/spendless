package com.jesushz.spendless.core.database.di

import androidx.room.Room
import com.jesushz.spendless.core.database.RoomLocalUserDataSource
import com.jesushz.spendless.core.database.SpendLessDatabase
import com.jesushz.spendless.core.domain.user.LocalUserDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            SpendLessDatabase::class.java,
            SpendLessDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration(false).build()
    }

    single { get<SpendLessDatabase>().userDao }

    singleOf(::RoomLocalUserDataSource).bind<LocalUserDataSource>()

}
