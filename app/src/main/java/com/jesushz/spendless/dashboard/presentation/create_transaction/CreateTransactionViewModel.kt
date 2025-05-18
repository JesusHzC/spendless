package com.jesushz.spendless.dashboard.presentation.create_transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.R
import com.jesushz.spendless.core.database.entity.TransactionEntity
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.transactions.LocalTransactionDataSource
import com.jesushz.spendless.core.domain.transactions.TransactionType
import com.jesushz.spendless.core.presentation.ui.UiText
import com.jesushz.spendless.core.presentation.ui.asUiText
import com.jesushz.spendless.core.util.Result
import com.jesushz.spendless.core.util.getDateFormat
import com.jesushz.spendless.core.util.isNumber
import com.jesushz.spendless.core.util.toNumber
import com.jesushz.spendless.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateTransactionViewModel(
    private val dataStoreManager: DataStoreManager,
    private val dashboardRepository: DashboardRepository
): ViewModel() {

    private val _state = MutableStateFlow(CreateTransactionState())
    val state = _state.asStateFlow()

    private val _event = Channel<CreateTransactionEvent>()
    val event = _event.receiveAsFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    userId = dataStoreManager.getUser()?.username.orEmpty(),
                    transactionsPreferences = dataStoreManager.getAllTransactionsPreferences()
                )
            }
        }
    }

    fun onAction(action: CreateTransactionAction) {
        when (action) {
            is CreateTransactionAction.OnAmountChange -> {
                if (!action.amount.isNumber())
                    return

                _state.update {
                    it.copy(
                        amount = action.amount
                    )
                }
            }
            is CreateTransactionAction.OnReceiverChange -> {
                _state.update {
                    it.copy(
                        receiver = action.receiver
                    )
                }
            }
            is CreateTransactionAction.OnNoteChange -> {
                _state.update {
                    it.copy(
                        note = action.note
                    )
                }
            }
            is CreateTransactionAction.OnTransactionTypeSelected -> {
                _state.update {
                    it.copy(
                        transactionType = action.transaction
                    )
                }
            }
            is CreateTransactionAction.OnCategorySelected -> {
                _state.update {
                    it.copy(
                        categorySelected = action.category
                    )
                }
            }
            is CreateTransactionAction.OnRepeatSelected -> {
                _state.update {
                    it.copy(
                        repeatSelected = action.repeat
                    )
                }
            }
            CreateTransactionAction.OnCreateTransactionClick -> {
                createTransaction()
            }
            else -> {
                Unit
            }
        }
    }

    private fun createTransaction() {
        when {
            state.value.amount.isEmpty() -> {
                viewModelScope.launch {
                    _event.send(CreateTransactionEvent.OnError(UiText.StringResource(R.string.amount_is_required)))
                }
                return
            }
            state.value.receiver.isEmpty() -> {
                viewModelScope.launch {
                    _event.send(CreateTransactionEvent.OnError(UiText.StringResource(R.string.receiver_is_required)))
                }
                return
            }
        }

        val userId = state.value.userId
        val transactionType = state.value.transactionType
        val category = if (transactionType == TransactionType.EXPENSE) {
            state.value.categorySelected
        } else null
        val amount = state.value.amount.toNumber()
        val receiver = state.value.receiver
        val note = state.value.note
        val dateTime = getDateFormat()
        val repeat = state.value.repeatSelected

        viewModelScope.launch(Dispatchers.IO) {
            val result = dashboardRepository
                .upsertTransaction(
                    userId = userId,
                    category = category,
                    amount = amount,
                    receiver = receiver,
                    note = note,
                    dateTime = dateTime,
                    repeat = repeat,
                    transactionType = transactionType
                )

            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Error -> {
                        _event.send(CreateTransactionEvent.OnError(result.error.asUiText()))
                    }
                    is Result.Success -> {
                        _event.send(CreateTransactionEvent.OnCreateTransactionSuccess)
                    }
                }
            }
        }
    }

}
