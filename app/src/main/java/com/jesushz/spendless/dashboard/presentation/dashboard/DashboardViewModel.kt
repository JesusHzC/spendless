package com.jesushz.spendless.dashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.R
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.transactions.CombineTransaction
import com.jesushz.spendless.core.util.Result
import com.jesushz.spendless.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

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

            val todayRepeatTransactions = dashboardRepository
                .getTodayRepeatTransactions(username)

            if (todayRepeatTransactions is Result.Success) {
                Timber.i("Today repeat transactions: ${todayRepeatTransactions.data}")
                for (transaction in todayRepeatTransactions.data) {
                    if (transaction.date.isNotEmpty()) {
                        dashboardRepository.upsertTransactionRepeat(username, transaction)
                        dashboardRepository.clearRepeatDateTime(transaction.oldTransactionId)
                    }
                }
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
                    .getLatestTransaction(username)
                    .mapNotNull { it }
                    .onEach { transaction ->
                        Timber.d("Latest transaction: $transaction")
                        _state.update {
                            it.copy(
                                latestTransaction = transaction
                            )
                        }
                    }
                    .launchIn(viewModelScope)

                combine(
                    flow = dashboardRepository.getTodayTransactions(username),
                    flow2 = dashboardRepository.getYesterdayTransactions(username)
                ) { todayTransactions, yesterdayTransactions ->
                    val today = CombineTransaction(
                        title = R.string.today_title_list,
                        transactions = todayTransactions
                    )
                    val yesterday = CombineTransaction(
                        title = R.string.yesterday_title_list,
                        transactions = yesterdayTransactions
                    )
                    Timber.d("Today: $today")
                    Timber.d("Yesterday: $yesterday")
                    _state.update {
                        it.copy(
                            latestTransactions = if (today.transactions.isNotEmpty() || yesterday.transactions.isNotEmpty()) {
                                listOf(today, yesterday)
                            } else {
                                emptyList()
                            }
                        )
                    }
                }.launchIn(viewModelScope)
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