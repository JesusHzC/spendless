package com.jesushz.spendless.dashboard.presentation.home

sealed interface HomAction {

    data object OnCreateTransactionClick: HomAction
    data object OnDismissTransactionClick: HomAction

}
