package com.jesushz.spendless

import android.app.Application
import com.jesushz.spendless.auth.di.authModule
import com.jesushz.spendless.core.database.di.databaseModule
import com.jesushz.spendless.core.di.coreModule
import com.jesushz.spendless.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class SpendLessApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SpendLessApp)
            modules(
                authModule,
                databaseModule,
                coreModule,
                appModule
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}
