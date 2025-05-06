package com.jesushz.spendless.core.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.preferences.PreferencesKeys
import com.jesushz.spendless.core.domain.user.User
import com.jesushz.spendless.dashboard.domain.Currency
import com.jesushz.spendless.dashboard.domain.DecimalSeparator
import com.jesushz.spendless.dashboard.domain.ExpenseFormat
import com.jesushz.spendless.dashboard.domain.ThousandSeparator
import kotlinx.coroutines.flow.first

class DefaultDataStoreManager(
    private val context: Context
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

    companion object {
        private const val DATA_STORE_FILE_NAME = "spendless.preferences_pb"
    }

}
