package com.jesushz.spendless.core.domain.security

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SessionManager(
    private val applicationScope: CoroutineScope,
) {
    private var lastUsed: Long = System.currentTimeMillis()

    private var sessionJob: Job? = null
    private var lockoutJob: Job? = null

    private val _sessionExpired = Channel<SessionExpiredType>()
    val sessionExpired = _sessionExpired.receiveAsFlow()

    fun startSessionMonitor(duration: Long) {
        stopSessionMonitor()
        sessionJob = launchMonitor(duration, SessionExpiredType.SESSION_EXPIRED)
    }

    fun stopSessionMonitor() {
        Timber.i("Stopping session monitor")
        sessionJob?.cancel()
        sessionJob = null
    }

    fun startLockoutMonitor(duration: Long) {
        stopLockoutMonitor()
        lockoutJob = launchMonitor(duration, SessionExpiredType.LOCKED_OUT)
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
                        _sessionExpired.send(type)
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
