package com.jesushz.spendless.core.domain.preferences

import com.jesushz.spendless.core.domain.user.User
import com.jesushz.spendless.dashboard.domain.Currency
import com.jesushz.spendless.dashboard.domain.DecimalSeparator
import com.jesushz.spendless.dashboard.domain.ExpenseFormat
import com.jesushz.spendless.dashboard.domain.ThousandSeparator

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

}
