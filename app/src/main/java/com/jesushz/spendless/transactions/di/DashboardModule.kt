package com.jesushz.spendless.transactions.di

import com.jesushz.spendless.transactions.data.repository.DashboardRepositoryImpl
import com.jesushz.spendless.transactions.domain.repository.DashboardRepository
import com.jesushz.spendless.transactions.presentation.all_transactions.AllTransactionsViewModel
import com.jesushz.spendless.transactions.presentation.create_transaction.CreateTransactionViewModel
import com.jesushz.spendless.transactions.presentation.dashboard.DashboardViewModel
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
