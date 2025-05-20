package com.jesushz.spendless

import android.app.Application
import com.jesushz.spendless.auth.di.authModule
import com.jesushz.spendless.core.database.di.databaseModule
import com.jesushz.spendless.core.di.coreModule
import com.jesushz.spendless.dashboard.di.dashboardModule
import com.jesushz.spendless.di.appModule
import com.jesushz.spendless.settings.di.settingsModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class SpendLessApp: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SpendLessApp)
            modules(
                authModule,
                databaseModule,
                coreModule,
                appModule,
                dashboardModule,
                settingsModule
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}
