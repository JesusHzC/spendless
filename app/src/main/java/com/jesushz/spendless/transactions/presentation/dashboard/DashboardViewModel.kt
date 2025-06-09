package com.jesushz.spendless.transactions.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.transactions.CombineTransaction
import com.jesushz.spendless.transactions.domain.repository.DashboardRepository
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
        dataStoreManager
            .getUser()
            .onEach { user ->
                _state.update {
                    it.copy(
                        username = user?.username.orEmpty()
                    )
                }
            }
            .launchIn(viewModelScope)

        dataStoreManager
            .getCurrency()
            .onEach { currency ->
                _state.update {
                    it.copy(
                        currency = currency
                    )
                }
            }
            .launchIn(viewModelScope)

        dataStoreManager
            .getExpenseFormat()
            .onEach { format ->
                _state.update {
                    it.copy(
                        expenseFormat = format
                    )
                }
            }
            .launchIn(viewModelScope)

        dataStoreManager
            .getDecimalSeparator()
            .onEach { separator ->
                _state.update {
                    it.copy(
                        decimalSeparator = separator
                    )
                }
            }
            .launchIn(viewModelScope)

        dataStoreManager
            .getThousandSeparator()
            .onEach { separator ->
                _state.update {
                    it.copy(
                        thousandSeparator = separator
                    )
                }
            }
            .launchIn(viewModelScope)

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
                    .getTodayTransactionsPending(username)
                    .onEach { transactions ->
                        Timber.d("Today repeat transactions: $transactions")
                        for (transaction in transactions) {
                            dashboardRepository.upsertTransaction(username, transaction)
                            dashboardRepository.deleteTransactionPendingById(transaction.id)
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
                        showCreateTransactionBottomSheet = false,
                        tmpTransaction = null
                    )
                }
            }
            is DashboardAction.OnEditTransaction -> {
                _state.update {
                    it.copy(
                        tmpTransaction = action.transaction,
                        showCreateTransactionBottomSheet = true
                    )
                }
            }
            is DashboardAction.OnDeleteTransaction -> {
                viewModelScope.launch(Dispatchers.IO) {
                    dashboardRepository.deleteTransactionById(action.transaction.id)
                    dashboardRepository.deleteTransactionPendingByParentId(action.transaction.id)
                }
            }
            DashboardAction.OnShowBalanceClick -> {
                _state.update {
                    it.copy(
                        shouldShowBalance = !it.shouldShowBalance
                    )
                }
            }
            DashboardAction.OnExportTransactionClick -> {
                _state.update {
                    it.copy(
                        showExportTransactionBottomSheet = true
                    )
                }
            }
            DashboardAction.OnDismissExportTransactionBottomSheet -> {
                _state.update {
                    it.copy(
                        showExportTransactionBottomSheet = false
                    )
                }
            }
            else -> Unit
        }
    }

}