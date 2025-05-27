package com.jesushz.spendless.dashboard.presentation.all_transactions

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
            .getThousandSeparator()
            .onEach { separator ->
                _state.update {
                    it.copy(
                        thousandSeparator = separator
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

        state
            .map { it.username }
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .onEach { username ->
                dashboardRepository
                    .getAllTransactions(username)
                    .onEach { transactions ->
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
                                allTransactions = newTransactions
                            )
                        }
                    }
                    .launchIn(viewModelScope)

                dashboardRepository
                    .getComingSoonTransactions(username)
                    .onEach { transactions ->
                        val newTransaction = transactions.map {
                            CombineTransaction(
                                date = "COMING SOON",
                                transactions = transactions
                            )
                        }
                        Timber.d("comingSoonTransactions: $newTransaction")
                        _state.update {
                            it.copy(
                                comingSoonTransactions = newTransaction
                            )
                        }
                    }
                    .launchIn(viewModelScope)
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: AllTransactionsAction) {
        when (action) {
            AllTransactionsAction.OnDismissTransactionClick -> {
                _state.update {
                    it.copy(
                        showCreateTransactionBottomSheet = false,
                        tmpTransaction = null
                    )
                }
            }
            is AllTransactionsAction.OnEditTransaction -> {
                _state.update {
                    it.copy(
                        showCreateTransactionBottomSheet = true,
                        tmpTransaction = action.transaction
                    )
                }
            }
            is AllTransactionsAction.OnDeleteTransaction -> {
                viewModelScope.launch(Dispatchers.IO) {
                    dashboardRepository.clearRepeatDateTime(action.transaction.id)
                }
            }
            else -> Unit
        }
    }

}