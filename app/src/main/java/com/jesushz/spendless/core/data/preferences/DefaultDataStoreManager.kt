package com.jesushz.spendless.core.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.preferences.PreferencesKeys
import com.jesushz.spendless.core.domain.user.User
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

    companion object {
        private const val DATA_STORE_FILE_NAME = "spendless.preferences_pb"
    }

}
