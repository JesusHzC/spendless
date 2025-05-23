package com.jesushz.spendless.core.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.preferences.PreferencesKeys
import com.jesushz.spendless.core.domain.preferences.SecurityPreferences
import com.jesushz.spendless.core.domain.user.User
import com.jesushz.spendless.core.domain.transactions.Currency
import com.jesushz.spendless.core.domain.transactions.DecimalSeparator
import com.jesushz.spendless.core.domain.transactions.ExpenseFormat
import com.jesushz.spendless.core.domain.transactions.ThousandSeparator
import com.jesushz.spendless.core.domain.preferences.TransactionsPreferences
import com.jesushz.spendless.core.domain.security.Biometrics
import com.jesushz.spendless.core.domain.security.LockedOutDuration
import com.jesushz.spendless.core.domain.security.SessionDuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
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

    override suspend fun getUser(): User? {
        val preferences = context.dataStore.data.first()
        val usernameKey = stringPreferencesKey(PreferencesKeys.USER_NAME)
        val userPinKey = stringPreferencesKey(PreferencesKeys.USER_PIN)
        return if (preferences[usernameKey].isNullOrEmpty() || preferences[userPinKey].isNullOrEmpty()) {
            null
        } else {
            User(
                username = preferences[usernameKey].orEmpty(),
                pin = preferences[userPinKey].orEmpty()
            )
        }
    }

    override suspend fun saveExpenseFormat(format: ExpenseFormat) {
        val expenseFormatKey = stringPreferencesKey(PreferencesKeys.EXPENSE_FORMAT)
        context.dataStore.edit { preferences ->
            preferences[expenseFormatKey] = format.name
        }
    }

    override suspend fun getExpenseFormat(): ExpenseFormat {
        val preferences = context.dataStore.data.first()
        val expenseFormatKey = stringPreferencesKey(PreferencesKeys.EXPENSE_FORMAT)
        return ExpenseFormat.valueOf(preferences[expenseFormatKey] ?: ExpenseFormat.POSITIVE.name)
    }

    override suspend fun saveCurrency(currency: Currency) {
        val currencyKey = stringPreferencesKey(PreferencesKeys.CURRENCY)
        context.dataStore.edit { preferences ->
            preferences[currencyKey] = currency.name
        }
    }

    override suspend fun getCurrency(): Currency {
        val preferences = context.dataStore.data.first()
        val currencyKey = stringPreferencesKey(PreferencesKeys.CURRENCY)
        return Currency.valueOf(preferences[currencyKey] ?: Currency.MEXICAN_PESO.name)
    }

    override suspend fun saveDecimalSeparator(separator: DecimalSeparator) {
        val decimalSeparatorKey = stringPreferencesKey(PreferencesKeys.DECIMAL_SEPARATOR)
        context.dataStore.edit { preferences ->
            preferences[decimalSeparatorKey] = separator.name
        }
    }

    override suspend fun getDecimalSeparator(): DecimalSeparator {
        val preferences = context.dataStore.data.first()
        val decimalSeparatorKey = stringPreferencesKey(PreferencesKeys.DECIMAL_SEPARATOR)
        return DecimalSeparator.valueOf(preferences[decimalSeparatorKey] ?: DecimalSeparator.POINT.name)
    }

    override suspend fun saveThousandSeparator(separator: ThousandSeparator) {
        val thousandSeparatorKey = stringPreferencesKey(PreferencesKeys.THOUSAND_SEPARATOR)
        context.dataStore.edit { preferences ->
            preferences[thousandSeparatorKey] = separator.name
        }
    }

    override suspend fun getThousandSeparator(): ThousandSeparator {
        val preferences = context.dataStore.data.first()
        val thousandSeparatorKey = stringPreferencesKey(PreferencesKeys.THOUSAND_SEPARATOR)
        return ThousandSeparator.valueOf(preferences[thousandSeparatorKey] ?: ThousandSeparator.SPACE.name)
    }

    override suspend fun saveAllTransactionsPreferences(preferences: TransactionsPreferences) {
        applicationScope.launch {
            saveCurrency(preferences.currency)
            saveDecimalSeparator(preferences.decimalSeparator)
            saveExpenseFormat(preferences.expenseFormat)
            saveThousandSeparator(preferences.thousandSeparator)
        }.join()
    }

    override suspend fun getAllTransactionsPreferences(): TransactionsPreferences {
        return TransactionsPreferences(
            currency = getCurrency(),
            expenseFormat = getExpenseFormat(),
            decimalSeparator = getDecimalSeparator(),
            thousandSeparator = getThousandSeparator()
        )
    }

    override suspend fun saveBiometrics(biometrics: Biometrics) {
        val biometricsKey = stringPreferencesKey(PreferencesKeys.BIOMETRICS)
        context.dataStore.edit { preferences ->
            preferences[biometricsKey] = biometrics.name
        }
    }

    override suspend fun getBiometrics(): Biometrics {
        val preferences = context.dataStore.data.first()
        val biometricsKey = stringPreferencesKey(PreferencesKeys.BIOMETRICS)
        return Biometrics.valueOf(preferences[biometricsKey] ?: Biometrics.DISABLE.name)
    }

    override suspend fun saveSessionDuration(duration: SessionDuration) {
        val sessionDurationKey = stringPreferencesKey(PreferencesKeys.SESSION_DURATION)
        context.dataStore.edit { preferences ->
            preferences[sessionDurationKey] = duration.name
        }
    }

    override suspend fun getSessionDuration(): SessionDuration {
        val preferences = context.dataStore.data.first()
        val sessionDurationKey = stringPreferencesKey(PreferencesKeys.SESSION_DURATION)
        return SessionDuration.valueOf(preferences[sessionDurationKey] ?: SessionDuration.FIVE_MINUTES.name)
    }

    override suspend fun saveLockedOutDuration(duration: LockedOutDuration) {
        val lockedOutDurationKey = stringPreferencesKey(PreferencesKeys.LOCKED_OUT_DURATION)
        context.dataStore.edit { preferences ->
            preferences[lockedOutDurationKey] = duration.name
        }
    }

    override suspend fun getLockedOutDuration(): LockedOutDuration {
        val preferences = context.dataStore.data.first()
        val lockedOutDurationKey = stringPreferencesKey(PreferencesKeys.LOCKED_OUT_DURATION)
        return LockedOutDuration.valueOf(preferences[lockedOutDurationKey] ?: LockedOutDuration.FIFTEEN_SECONDS.name)
    }

    override suspend fun saveAllSecurityPreferences(preferences: SecurityPreferences) {
        applicationScope.launch {
            saveBiometrics(preferences.biometrics)
            saveSessionDuration(preferences.sessionDuration)
            saveLockedOutDuration(preferences.lockedOutDuration)
        }.join()
    }

    override suspend fun getAllSecurityPreferences(): SecurityPreferences {
        return SecurityPreferences(
            biometrics = getBiometrics(),
            sessionDuration = getSessionDuration(),
            lockedOutDuration = getLockedOutDuration()
        )
    }

    override suspend fun clearAllPreferences() {
        applicationScope.launch {
            val userNameKey = stringPreferencesKey(PreferencesKeys.USER_NAME)
            val userPinKey = stringPreferencesKey(PreferencesKeys.USER_PIN)
            val expenseFormatKey = stringPreferencesKey(PreferencesKeys.EXPENSE_FORMAT)
            val currencyKey = stringPreferencesKey(PreferencesKeys.CURRENCY)
            val decimalSeparatorKey = stringPreferencesKey(PreferencesKeys.DECIMAL_SEPARATOR)
            val thousandSeparatorKey = stringPreferencesKey(PreferencesKeys.THOUSAND_SEPARATOR)
            val biometricsKey = stringPreferencesKey(PreferencesKeys.BIOMETRICS)
            val sessionDurationKey = stringPreferencesKey(PreferencesKeys.SESSION_DURATION)
            val lockedOutDurationKey = stringPreferencesKey(PreferencesKeys.LOCKED_OUT_DURATION)

            context.dataStore.edit { preferences ->
                preferences.remove(userNameKey)
                preferences.remove(userPinKey)
                preferences.remove(expenseFormatKey)
                preferences.remove(currencyKey)
                preferences.remove(decimalSeparatorKey)
                preferences.remove(thousandSeparatorKey)
                preferences.remove(biometricsKey)
                preferences.remove(sessionDurationKey)
                preferences.remove(lockedOutDurationKey)
            }
        }
    }

    companion object {
        private const val DATA_STORE_FILE_NAME = "spendless.preferences_pb"
    }

}
