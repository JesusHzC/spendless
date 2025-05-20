package com.jesushz.spendless.settings.di

import com.jesushz.spendless.settings.presentation.preferences.PreferencesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsModule = module {
    viewModelOf(::PreferencesViewModel)
}
