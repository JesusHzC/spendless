package com.jesushz.spendless.core.domain.preferences

import com.jesushz.spendless.core.domain.security.Biometrics
import com.jesushz.spendless.core.domain.security.LockedOutDuration
import com.jesushz.spendless.core.domain.security.SessionDuration
import com.jesushz.spendless.core.domain.user.User
import com.jesushz.spendless.core.domain.transactions.Currency
import com.jesushz.spendless.core.domain.transactions.DecimalSeparator
import com.jesushz.spendless.core.domain.transactions.ExpenseFormat
import com.jesushz.spendless.core.domain.transactions.ThousandSeparator

interface DataStoreManager {

    suspend fun saveUser(user: User)
    suspend fun getUser(): User?
    suspend fun saveExpenseFormat(format: ExpenseFormat)
    suspend fun getExpenseFormat(): ExpenseFormat
    suspend fun saveCurrency(currency: Currency)
    suspend fun getCurrency(): Currency
    suspend fun saveDecimalSeparator(separator: DecimalSeparator)
    suspend fun getDecimalSeparator(): DecimalSeparator
    suspend fun saveThousandSeparator(separator: ThousandSeparator)
    suspend fun getThousandSeparator(): ThousandSeparator
    suspend fun saveAllTransactionsPreferences(preferences: TransactionsPreferences)
    suspend fun getAllTransactionsPreferences(): TransactionsPreferences
    suspend fun saveBiometrics(biometrics: Biometrics)
    suspend fun getBiometrics(): Biometrics
    suspend fun saveSessionDuration(duration: SessionDuration)
    suspend fun getSessionDuration(): SessionDuration
    suspend fun saveLockedOutDuration(duration: LockedOutDuration)
    suspend fun getLockedOutDuration(): LockedOutDuration
    suspend fun saveAllSecurityPreferences(preferences: SecurityPreferences)
    suspend fun getAllSecurityPreferences(): SecurityPreferences
    suspend fun clearAllPreferences()

}
