package com.jesushz.spendless.transactions.di

import com.jesushz.spendless.transactions.data.repository.DashboardRepositoryImpl
import com.jesushz.spendless.transactions.domain.ExportTransactionsUseCase
import com.jesushz.spendless.transactions.domain.repository.DashboardRepository
import com.jesushz.spendless.transactions.notification.ExportSuccessNotification
import com.jesushz.spendless.transactions.presentation.all_transactions.AllTransactionsViewModel
import com.jesushz.spendless.transactions.presentation.create_transaction.CreateTransactionViewModel
import com.jesushz.spendless.transactions.presentation.dashboard.DashboardViewModel
import com.jesushz.spendless.transactions.presentation.export_transactions.ExportTransactionsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dashboardModule = module {
    viewModelOf(::DashboardViewModel)
    viewModelOf(::CreateTransactionViewModel)
    viewModelOf(::AllTransactionsViewModel)
    viewModelOf(::ExportTransactionsViewModel)

    singleOf(::DashboardRepositoryImpl).bind<DashboardRepository>()

    single {
        ExportTransactionsUseCase(androidContext())
    }

    single {
        ExportSuccessNotification(androidContext())
    }
}
