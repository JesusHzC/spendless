package com.jesushz.spendless

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(
                isLoggedIn = dataStoreManager.getUser() != null
            )
        }
    }

}