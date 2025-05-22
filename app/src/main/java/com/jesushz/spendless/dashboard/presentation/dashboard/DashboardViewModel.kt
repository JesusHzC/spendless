package com.jesushz.spendless.dashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.transactions.CombineTransaction
import com.jesushz.spendless.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DashboardViewModel(
    private val dataStoreManager: DataStoreManager,
    private val dashboardRepository: DashboardRepository
): ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val username = dataStoreManager.getUser()?.username.orEmpty()

            _state.update {
                it.copy(
                    username = username,
                    currency = dataStoreManager.getCurrency(),
                    thousandSeparator = dataStoreManager.getThousandSeparator(),
                    expenseFormat = dataStoreManager.getExpenseFormat(),
                    decimalSeparator = dataStoreManager.getDecimalSeparator()
                )
            }
            _state.update { it.copy(isLoading = false) }
        }

        state
            .map { it.username }
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .onEach { username ->
                Timber.d("Username: $username")
                dashboardRepository
                    .getAccountBalance(username)
                    .mapNotNull { it }
                    .onEach { balance ->
                        Timber.d("Balance: $balance")
                        _state.update {
                            it.copy(
                                accountBalance = balance
                            )
                        }
                    }
                    .launchIn(viewModelScope)

                dashboardRepository
                    .getLongestTransaction(username)
                    .mapNotNull { it }
                    .onEach { transaction ->
                        Timber.d("Longest transaction: $transaction")
                        _state.update {
                            it.copy(
                                longestTransaction = transaction
                            )
                        }
                    }
                    .launchIn(viewModelScope)

                dashboardRepository
                    .getPreviousWeekBalance(username)
                    .mapNotNull { it }
                    .onEach { balance ->
                        Timber.d("Previous week balance: $balance")
                        _state.update {
                            it.copy(
                                previousWeekBalance = balance
                            )
                        }
                    }
                    .launchIn(viewModelScope)

                dashboardRepository
                    .getTodayRepeatTransactions(username)
                    .onEach { transactions ->
                        Timber.d("Today repeat transactions: $transactions")
                        for (transaction in transactions) {
                            if (transaction.date.isNotEmpty()) {
                                dashboardRepository.upsertTransactionRepeat(username, transaction)
                                dashboardRepository.clearRepeatDateTime(transaction.oldTransactionId)
                            }
                        }
                    }
                    .launchIn(viewModelScope)

                dashboardRepository
                    .getLatestTransactions(username)
                    .onEach { transactions ->
                        Timber.d("Latest transactions: $transactions")
                        val today = LocalDate.now()
                        val yesterday = today.minusDays(1)
                        val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                        val outputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.getDefault())

                        val newTransactions = transactions
                            .groupBy { LocalDateTime.parse(it.date, inputFormatter).toLocalDate() }
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
                                latestTransactions = newTransactions,
                                latestTransaction = if (transactions.isNotEmpty()) {
                                    transactions.first()
                                } else {
                                    null
                                }
                            )
                        }
                    }
                    .launchIn(viewModelScope)

            }.launchIn(viewModelScope)
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.OnCreateTransactionClick -> {
                _state.update {
                    it.copy(
                        showCreateTransactionBottomSheet = true
                    )
                }
            }
            DashboardAction.OnDismissTransactionClick -> {
                _state.update {
                    it.copy(
                        showCreateTransactionBottomSheet = false
                    )
                }
            }
            else -> Unit
        }
    }

}