package com.jesushz.spendless.auth.di

import com.jesushz.spendless.auth.domain.UsernameValidator
import com.jesushz.spendless.auth.presentation.login.LoginViewModel
import com.jesushz.spendless.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    singleOf(::UsernameValidator)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}
