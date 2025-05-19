package com.jesushz.spendless.dashboard.di

import com.jesushz.spendless.dashboard.data.repository.DashboardRepositoryImpl
import com.jesushz.spendless.dashboard.domain.repository.DashboardRepository
import com.jesushz.spendless.dashboard.presentation.all_transactions.AllTransactionsViewModel
import com.jesushz.spendless.dashboard.presentation.create_transaction.CreateTransactionViewModel
import com.jesushz.spendless.dashboard.presentation.home.HomeViewModel
import com.jesushz.spendless.dashboard.presentation.preferences.PreferencesViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dashboardModule = module {
    viewModelOf(::PreferencesViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::CreateTransactionViewModel)
    viewModelOf(::AllTransactionsViewModel)

    singleOf(::DashboardRepositoryImpl).bind<DashboardRepository>()
}
