package com.jesushz.spendless.dashboard.presentation.create_transaction

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateTransactionViewModel: ViewModel() {

    private val _state = MutableStateFlow(CreateTransactionState())
    val state = _state.asStateFlow()

}
