package com.jesushz.spendless.dashboard.di

import com.jesushz.spendless.dashboard.data.repository.DashboardRepositoryImpl
import com.jesushz.spendless.dashboard.domain.repository.DashboardRepository
import com.jesushz.spendless.dashboard.presentation.all_transactions.AllTransactionsViewModel
import com.jesushz.spendless.dashboard.presentation.create_transaction.CreateTransactionViewModel
import com.jesushz.spendless.dashboard.presentation.dashboard.DashboardViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dashboardModule = module {
    viewModelOf(::DashboardViewModel)
    viewModelOf(::CreateTransactionViewModel)
    viewModelOf(::AllTransactionsViewModel)

    singleOf(::DashboardRepositoryImpl).bind<DashboardRepository>()
}
