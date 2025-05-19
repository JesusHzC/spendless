package com.jesushz.spendless.dashboard.presentation.all_transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.transactions.CombineTransaction
import com.jesushz.spendless.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AllTransactionsViewModel(
    private val dataStoreManager: DataStoreManager,
    private val dashboardRepository: DashboardRepository
): ViewModel() {

    private val _state = MutableStateFlow(AllTransactionsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    username = dataStoreManager.getUser()?.username.orEmpty(),
                    currency = dataStoreManager.getCurrency(),
                    thousandSeparator = dataStoreManager.getThousandSeparator(),
                    expenseFormat = dataStoreManager.getExpenseFormat(),
                    decimalSeparator = dataStoreManager.getDecimalSeparator()
                )
            }
        }.invokeOnCompletion {
            dashboardRepository
                .getAllTransactions(state.value.username)
                .onEach { transactions ->
                    val today = LocalDate.now()
                    val yesterday = today.minusDays(1)
                    val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    val outputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.getDefault())

                    val newTransactions = transactions
                        .groupBy { LocalDateTime.parse(it.dateTime, inputFormatter).toLocalDate() }
                        .toSortedMap(compareByDescending { it })
                        .map { (date, transaction) ->
                            val title = when (date) {
                                today -> "TODAY"
                                yesterday -> "YESTERDAY"
                                else -> date.format(outputFormatter).uppercase()
                            }
                            CombineTransaction(
                                date = title,
                                transactions = transaction
                            )
                        }

                    Timber.d("newTransactions: $newTransactions")
                    _state.update {
                        it.copy(
                            allTransactions = newTransactions
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

}