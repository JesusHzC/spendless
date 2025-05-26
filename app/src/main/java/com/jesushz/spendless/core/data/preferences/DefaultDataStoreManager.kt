package com.jesushz.spendless.core.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.preferences.PreferencesKeys
import com.jesushz.spendless.core.domain.user.User
import com.jesushz.spendless.core.domain.transactions.Currency
import com.jesushz.spendless.core.domain.transactions.DecimalSeparator
import com.jesushz.spendless.core.domain.transactions.ExpenseFormat
import com.jesushz.spendless.core.domain.transactions.ThousandSeparator
import com.jesushz.spendless.core.domain.security.Biometrics
import com.jesushz.spendless.core.domain.security.LockedOutDuration
import com.jesushz.spendless.core.domain.security.SessionDuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DefaultDataStoreManager(
    private val context: Context,
    private val applicationScope: CoroutineScope
): DataStoreManager {

    private val Context.dataStore by preferencesDataStore(name = DATA_STORE_FILE_NAME)

    override suspend fun saveUser(user: User) {
        val userNameKey = stringPreferencesKey(PreferencesKeys.USER_NAME)
        val userPinKey = stringPreferencesKey(PreferencesKeys.USER_PIN)
        context.dataStore.edit { preferences ->
            preferences[userNameKey] = user.username
            preferences[userPinKey] = user.pin
        }
    }

    override fun getUser(): Flow<User?> {
        val usernameKey = stringPreferencesKey(PreferencesKeys.USER_NAME)
        val userPinKey = stringPreferencesKey(PreferencesKeys.USER_PIN)
        return context.dataStore.data.map { preferences ->
            val username = preferences[usernameKey]
            val pin = preferences[userPinKey]
            if (username.isNullOrEmpty() || pin.isNullOrEmpty()) null else User(username, pin)
        }
    }

    override suspend fun saveExpenseFormat(format: ExpenseFormat) {
        val expenseFormatKey = stringPreferencesKey(PreferencesKeys.EXPENSE_FORMAT)
        context.dataStore.edit { preferences ->
            preferences[expenseFormatKey] = format.name
        }
    }

    override fun getExpenseFormat(): Flow<ExpenseFormat> {
        val key = stringPreferencesKey(PreferencesKeys.EXPENSE_FORMAT)
        return context.dataStore.data.map { preferences ->
            ExpenseFormat.valueOf(preferences[key] ?: ExpenseFormat.POSITIVE.name)
        }
    }

    override suspend fun saveCurrency(currency: Currency) {
        val currencyKey = stringPreferencesKey(PreferencesKeys.CURRENCY)
        context.dataStore.edit { preferences ->
            preferences[currencyKey] = currency.name
        }
    }

    override fun getCurrency(): Flow<Currency> {
        val key = stringPreferencesKey(PreferencesKeys.CURRENCY)
        return context.dataStore.data.map { preferences ->
            Currency.valueOf(preferences[key] ?: Currency.MEXICAN_PESO.name)
        }
    }

    override suspend fun saveDecimalSeparator(separator: DecimalSeparator) {
        val decimalSeparatorKey = stringPreferencesKey(PreferencesKeys.DECIMAL_SEPARATOR)
        context.dataStore.edit { preferences ->
            preferences[decimalSeparatorKey] = separator.name
        }
    }

    override fun getDecimalSeparator(): Flow<DecimalSeparator> {
        val key = stringPreferencesKey(PreferencesKeys.DECIMAL_SEPARATOR)
        return context.dataStore.data.map { preferences ->
            DecimalSeparator.valueOf(preferences[key] ?: DecimalSeparator.POINT.name)
        }
    }

    override suspend fun saveThousandSeparator(separator: ThousandSeparator) {
        val thousandSeparatorKey = stringPreferencesKey(PreferencesKeys.THOUSAND_SEPARATOR)
        context.dataStore.edit { preferences ->
            preferences[thousandSeparatorKey] = separator.name
        }
    }

    override fun getThousandSeparator(): Flow<ThousandSeparator> {
        val key = stringPreferencesKey(PreferencesKeys.THOUSAND_SEPARATOR)
        return context.dataStore.data.map { preferences ->
            ThousandSeparator.valueOf(preferences[key] ?: ThousandSeparator.SPACE.name)
        }
    }

    override suspend fun saveBiometrics(biometrics: Biometrics) {
        val biometricsKey = stringPreferencesKey(PreferencesKeys.BIOMETRICS)
        context.dataStore.edit { preferences ->
            preferences[biometricsKey] = biometrics.name
        }
    }

    override fun getBiometrics(): Flow<Biometrics> {
        val key = stringPreferencesKey(PreferencesKeys.BIOMETRICS)
        return context.dataStore.data.map { preferences ->
            Biometrics.valueOf(preferences[key] ?: Biometrics.DISABLE.name)
        }
    }

    override suspend fun saveSessionDuration(duration: SessionDuration) {
        val sessionDurationKey = stringPreferencesKey(PreferencesKeys.SESSION_DURATION)
        context.dataStore.edit { preferences ->
            preferences[sessionDurationKey] = duration.name
        }
    }

    override fun getSessionDuration(): Flow<SessionDuration> {
        val key = stringPreferencesKey(PreferencesKeys.SESSION_DURATION)
        return context.dataStore.data.map { preferences ->
            SessionDuration.valueOf(preferences[key] ?: SessionDuration.FIVE_MINUTES.name)
        }
    }

    override suspend fun saveLockedOutDuration(duration: LockedOutDuration) {
        val lockedOutDurationKey = stringPreferencesKey(PreferencesKeys.LOCKED_OUT_DURATION)
        context.dataStore.edit { preferences ->
            preferences[lockedOutDurationKey] = duration.name
        }
    }

    override fun getLockedOutDuration(): Flow<LockedOutDuration> {
        val key = stringPreferencesKey(PreferencesKeys.LOCKED_OUT_DURATION)
        return context.dataStore.data.map { preferences ->
            LockedOutDuration.valueOf(preferences[key] ?: LockedOutDuration.FIFTEEN_SECONDS.name)
        }
    }

    override suspend fun clearUserData() {
        applicationScope.launch {
            val userNameKey = stringPreferencesKey(PreferencesKeys.USER_NAME)
            val userPinKey = stringPreferencesKey(PreferencesKeys.USER_PIN)

            context.dataStore.edit { preferences ->
                preferences.remove(userNameKey)
                preferences.remove(userPinKey)
            }
        }.join()
    }

    companion object {
        private const val DATA_STORE_FILE_NAME = "spendless.preferences_pb"
    }

}
