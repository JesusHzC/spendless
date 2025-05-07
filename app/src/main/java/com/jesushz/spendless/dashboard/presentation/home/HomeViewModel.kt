package com.jesushz.spendless.dashboard.presentation.home

import androidx.lifecycle.ViewModel
import com.jesushz.spendless.dashboard.domain.Preferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val preferences: Preferences
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun onAction(action: HomAction) {
        when (action) {
            HomAction.OnCreateTransactionClick -> {
                _state.update {
                    it.copy(
                        showCreateTransactionBottomSheet = true
                    )
                }
            }
            HomAction.OnDismissTransactionClick -> {
                _state.update {
                    it.copy(
                        showCreateTransactionBottomSheet = false
                    )
                }
            }
        }
    }

}