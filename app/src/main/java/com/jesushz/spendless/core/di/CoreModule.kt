package com.jesushz.spendless.core.di

import com.jesushz.spendless.core.data.preferences.DefaultDataStoreManager
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.security.SessionManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    singleOf(::DefaultDataStoreManager).bind<DataStoreManager>()

    single {
        SessionManager(get())
    }
}