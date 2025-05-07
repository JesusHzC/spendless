package com.jesushz.spendless.dashboard.di

import com.jesushz.spendless.dashboard.domain.Preferences
import com.jesushz.spendless.dashboard.presentation.home.HomeViewModel
import com.jesushz.spendless.dashboard.presentation.preferences.PreferencesViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dashboardModule = module {
    viewModelOf(::PreferencesViewModel)
    viewModelOf(::HomeViewModel)
    singleOf(::Preferences)
}
