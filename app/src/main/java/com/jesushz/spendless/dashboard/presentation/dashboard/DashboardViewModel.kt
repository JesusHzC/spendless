package com.jesushz.spendless.dashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.R
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.transactions.CombineTransaction
import com.jesushz.spendless.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
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
            Timber.d("Username: ${state.value.username}")
            dashboardRepository
                .getAccountBalance(state.value.username)
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
                .getLongestTransaction(state.value.username)
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
                .getLatestTransaction(state.value.username)
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
                flow = dashboardRepository.getTodayTransactions(state.value.username),
                flow2 = dashboardRepository.getYesterdayTransactions(state.value.username)
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
        }
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