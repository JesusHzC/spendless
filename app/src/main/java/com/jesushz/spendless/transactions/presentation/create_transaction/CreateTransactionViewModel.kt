@file:OptIn(ExperimentalUuidApi::class)

package com.jesushz.spendless.transactions.presentation.create_transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.R
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.transactions.Category
import com.jesushz.spendless.core.domain.transactions.Repeat
import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.domain.transactions.TransactionType
import com.jesushz.spendless.core.presentation.ui.UiText
import com.jesushz.spendless.core.presentation.ui.asUiText
import com.jesushz.spendless.core.util.Result
import com.jesushz.spendless.core.util.getDateFormat
import com.jesushz.spendless.core.util.toNumber
import com.jesushz.spendless.transactions.domain.repository.DashboardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CreateTransactionViewModel(
    private val dataStoreManager: DataStoreManager,
    private val dashboardRepository: DashboardRepository
): ViewModel() {

    private val _state = MutableStateFlow(CreateTransactionState())
    val state = _state.asStateFlow()

    private val _event = Channel<CreateTransactionEvent>()
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
    }

    fun onAction(action: CreateTransactionAction) {
        when (action) {
            is CreateTransactionAction.OnAmountChange -> {
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
            is CreateTransactionAction.OnDateSelected -> {
                Timber.i("Date selected: ${action.date}")
                _state.update {
                    it.copy(
                        dateSelected = action.date
                    )
                }
            }
            is CreateTransactionAction.OnUpdateTransaction -> {
                _state.update {
                    it.copy(
                        id = action.transaction.id,
                        amount = action.transaction.amount.toString(),
                        receiver = action.transaction.receiver,
                        note = action.transaction.note,
                        transactionType = action.transaction.transactionType,
                        categorySelected = action.transaction.category ?: Category.OTHER,
                        repeatSelected = action.transaction.repeat,
                        dateSelected = action.transaction.date,
                        isUpdate = true
                    )
                }
            }
            CreateTransactionAction.OnClose -> {
                clearState()
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
        val transaction = Transaction(
            id = state.value.id ?: Uuid.random().toString(),
            transactionType = state.value.transactionType,
            category = if (state.value.transactionType == TransactionType.EXPENSE) {
                state.value.categorySelected
            } else null,
            amount = state.value.amount.toNumber(),
            receiver = state.value.receiver,
            note = state.value.note,
            date = state.value.dateSelected,
            repeat = state.value.repeatSelected
        )

        viewModelScope.launch(Dispatchers.IO) {
            val result = dashboardRepository
                .upsertTransaction(
                    userId = userId,
                    transaction = transaction
                )

            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Error -> {
                        _event.send(CreateTransactionEvent.OnError(result.error.asUiText()))
                    }
                    is Result.Success -> {
                        _event.send(CreateTransactionEvent.OnCreateTransactionSuccess)
                        clearState()
                    }
                }
            }
        }
    }

    private fun clearState() {
        _state.update {
            it.copy(
                id = null,
                amount = "",
                receiver = "",
                note = "",
                transactionType = TransactionType.EXPENSE,
                categorySelected = Category.OTHER,
                repeatSelected = Repeat.NOT_REPEAT,
                dateSelected = getDateFormat(),
                isUpdate = false
            )
        }
    }

}
