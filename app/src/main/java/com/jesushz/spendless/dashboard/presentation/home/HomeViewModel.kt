package com.jesushz.spendless.dashboard.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.dashboard.domain.Preferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val preferences: Preferences,
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            preferences.initialize()
            _state.update {
                it.copy(
                    username = dataStoreManager.getUser()?.username.orEmpty()
                )
            }
        }
    }

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