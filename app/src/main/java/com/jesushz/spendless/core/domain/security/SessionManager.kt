package com.jesushz.spendless.core.domain.security

import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SessionManager(
    private val dataStoreManager: DataStoreManager,
    private val applicationScope: CoroutineScope,
    private val onSessionExpired: (SessionExpiredType) -> Unit,
) {
    private var lastUsed: Long = System.currentTimeMillis()

    private var sessionJob: Job? = null
    private var lockoutJob: Job? = null

    fun startSessionMonitor() {
        sessionJob?.cancel()
        applicationScope.launch {
            val duration = dataStoreManager.getSessionDuration().first().millis
            val isEnabled = dataStoreManager.isSessionMonitorEnabled().first()

            if (isEnabled) {
                Timber.i("Starting session monitor")
                sessionJob = launchMonitor(duration, SessionExpiredType.SESSION_EXPIRED)
            }
        }
    }

    fun stopSessionMonitor() {
        Timber.i("Stopping session monitor")
        sessionJob?.cancel()
        sessionJob = null
    }

    fun startLockoutMonitor() {
        lockoutJob?.cancel()
        applicationScope.launch {
            val duration = dataStoreManager.getLockedOutDuration().first().millis
            val isEnabled = dataStoreManager.isLockOutEnabled().first()

            if (isEnabled) {
                Timber.i("Starting lockout monitor")
                lockoutJob = launchMonitor(duration, SessionExpiredType.LOCKED_OUT)
            }
        }
    }

    fun stopLockoutMonitor() {
        Timber.i("Stopping lockout monitor")
        lockoutJob?.cancel()
        lockoutJob = null
    }

    private fun launchMonitor(duration: Long, type: SessionExpiredType): Job {
        return applicationScope.launch(Dispatchers.Default) {
            while (isActive) {
                val idle = System.currentTimeMillis() - lastUsed
                Timber.i("[$type] Idle time: $idle ms (limit: $duration ms)")
                if (idle > duration) {
                    withContext(Dispatchers.Main) {
                        onSessionExpired(type)
                    }
                    break
                }
                delay(SLEEP_TIME)
            }
        }
    }

    @Synchronized
    fun touch() {
        lastUsed = System.currentTimeMillis()
        Timber.d("Session touched: $lastUsed")
    }

    companion object {
        private const val SLEEP_TIME = 1000L
    }
}
