package com.jesushz.spendless.di

import com.jesushz.spendless.MainViewModel
import com.jesushz.spendless.SpendLessApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)

    single<CoroutineScope> {
        (androidApplication() as SpendLessApp).applicationScope
    }
}
