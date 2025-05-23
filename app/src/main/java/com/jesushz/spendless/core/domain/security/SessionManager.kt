package com.jesushz.spendless.core.domain.security

import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SessionManager(
    private val dataStoreManager: DataStoreManager,
    private val applicationScope: CoroutineScope,
    private val onLockOut: () -> Unit,
    private val onSessionExpired: () -> Unit
) {

    private var lastUsed: Long = System.currentTimeMillis()
    private var idleMonitorJob: Job? = null

    fun start() {
        applicationScope.launch {
            val lockedOutDuration = dataStoreManager.getLockedOutDuration()
            val sessionDuration = dataStoreManager.getSessionDuration()

            Timber.i("Starting idle monitor with lockout: ${lockedOutDuration.millis}, session: ${sessionDuration.millis}")
            startIdleMonitor(lockedOutDuration.millis, sessionDuration.millis)
        }
    }

    private fun startIdleMonitor(lockoutMillis: Long, sessionMillis: Long) {
        stop()
        idleMonitorJob = applicationScope.launch(Dispatchers.Default) {
            while (isActive) {
                val idle = System.currentTimeMillis() - lastUsed
                Timber.i("Idle time: $idle")

                if (idle > sessionMillis) {
                    withContext(Dispatchers.Main) {
                        onSessionExpired()
                        dataStoreManager.clearAllPreferences()
                    }
                    stop()
                } else if (idle > lockoutMillis) {
                    withContext(Dispatchers.Main) { onLockOut() }
                    stop()
                }

                delay(SLEEP_TIME)
            }
        }
    }

    fun stop() {
        Timber.i("Stopping idle monitor")
        idleMonitorJob?.cancel()
        idleMonitorJob = null
    }

    fun touch() {
        lastUsed = System.currentTimeMillis()
    }

    companion object {
        private const val SLEEP_TIME = 1000L
    }
}
