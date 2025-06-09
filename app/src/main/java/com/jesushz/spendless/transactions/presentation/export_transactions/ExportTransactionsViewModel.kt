package com.jesushz.spendless.transactions.presentation.export_transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.transactions.Export
import com.jesushz.spendless.core.presentation.ui.UiText
import com.jesushz.spendless.transactions.domain.ExportTransactionsUseCase
import com.jesushz.spendless.transactions.domain.repository.DashboardRepository
import com.jesushz.spendless.transactions.notification.ExportSuccessNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExportTransactionsViewModel(
    private val dataStoreManager: DataStoreManager,
    private val dashboardRepository: DashboardRepository,
    private val exportTransactionsUseCase: ExportTransactionsUseCase,
    private val exportSuccessNotification: ExportSuccessNotification
): ViewModel() {

    private val _state = MutableStateFlow(ExportTransactionsState())
    val state = _state.asStateFlow()

    private val _event = Channel<ExportTransactionsEvent>()
    val event = _event.receiveAsFlow()

    init {
        dataStoreManager
            .getUser()
            .onEach { user ->
                _state.update {
                    it.copy(
                        userId = user?.username.orEmpty()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ExportTransactionsAction) {
        when (action) {
            is ExportTransactionsAction.OnExportSelected -> {
                _state.update {
                    it.copy(
                        exportSelected = action.export
                    )
                }
            }
            ExportTransactionsAction.OnExportTransactions -> {
                if (state.value.userId.isNullOrEmpty()) return

                val userId = state.value.userId!!
                viewModelScope.launch(Dispatchers.IO) {
                    val transactions = async {
                        when (state.value.exportSelected) {
                            Export.LAST_THREE_MONTHS -> {
                                dashboardRepository.getLastThreeMonthsTransactions(userId)
                            }
                            Export.LAST_MONTH -> {
                                dashboardRepository.getLastMonthTransactions(userId)
                            }
                            Export.CURRENT_MONTH -> {
                                dashboardRepository.getCurrentMonthTransactions(userId)
                            }
//                            Export.CUSTOM_DATE_RANGE -> {
//                                dashboardRepository.getTransactionsByCustomDateRange(
//                                    userId,
//                                    startDate = "",
//                                    endDate = "",
//                                )
//                            }
                        }
                    }
                    exportTransactionsUseCase.execute(transactions.await())
                        .fold(
                            onSuccess = { uri ->
                                withContext(Dispatchers.Main) {
                                    exportSuccessNotification.execute(uri)
                                    _state.update {
                                        it.copy(
                                            exportSelected = Export.LAST_THREE_MONTHS
                                        )
                                    }
                                    _event.send(ExportTransactionsEvent.OnExportSuccess)
                                }
                            },
                            onFailure = {
                                withContext(Dispatchers.Main) {
                                    _event.send(
                                        ExportTransactionsEvent.OnExportError(
                                            error = UiText.DynamicString("An error occurred while exporting transactions, please try again.")
                                        )
                                    )
                                }
                            }
                        )
                }
            }
            else -> Unit
        }
    }

}
