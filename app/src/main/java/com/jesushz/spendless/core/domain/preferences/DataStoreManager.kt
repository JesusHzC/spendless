package com.jesushz.spendless.core.domain.preferences

import com.jesushz.spendless.core.domain.security.Biometrics
import com.jesushz.spendless.core.domain.security.LockedOutDuration
import com.jesushz.spendless.core.domain.security.SessionDuration
import com.jesushz.spendless.core.domain.user.User
import com.jesushz.spendless.core.domain.transactions.Currency
import com.jesushz.spendless.core.domain.transactions.DecimalSeparator
import com.jesushz.spendless.core.domain.transactions.ExpenseFormat
import com.jesushz.spendless.core.domain.transactions.ThousandSeparator
import kotlinx.coroutines.flow.Flow

interface DataStoreManager {

    suspend fun saveUser(user: User)
    fun getUser(): Flow<User?>
    suspend fun saveExpenseFormat(format: ExpenseFormat)
    fun getExpenseFormat(): Flow<ExpenseFormat>
    suspend fun saveCurrency(currency: Currency)
    fun getCurrency(): Flow<Currency>
    suspend fun saveDecimalSeparator(separator: DecimalSeparator)
    fun getDecimalSeparator(): Flow<DecimalSeparator>
    suspend fun saveThousandSeparator(separator: ThousandSeparator)
    fun getThousandSeparator(): Flow<ThousandSeparator>
    suspend fun saveBiometrics(biometrics: Biometrics)
    fun getBiometrics(): Flow<Biometrics>
    suspend fun saveSessionDuration(duration: SessionDuration)
    fun getSessionDuration(): Flow<SessionDuration>
    suspend fun saveLockedOutDuration(duration: LockedOutDuration)
    fun getLockedOutDuration(): Flow<LockedOutDuration>
    suspend fun updateSessionMonitorEnabled(isEnabled: Boolean)
    fun isSessionMonitorEnabled(): Flow<Boolean>
    suspend fun updateLockOutEnabled(isEnabled: Boolean)
    fun isLockOutEnabled(): Flow<Boolean>
    suspend fun clearUserData()

}
