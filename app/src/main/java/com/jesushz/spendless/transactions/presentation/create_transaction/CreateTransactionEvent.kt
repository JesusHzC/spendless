package com.jesushz.spendless.transactions.presentation.create_transaction

import com.jesushz.spendless.core.presentation.ui.UiText

sealed interface CreateTransactionEvent {

    data class OnError(val error: UiText): CreateTransactionEvent
    data object OnCreateTransactionSuccess: CreateTransactionEvent

}
