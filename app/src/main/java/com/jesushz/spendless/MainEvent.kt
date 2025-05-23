package com.jesushz.spendless

sealed interface MainEvent {

    data object OnNavigateToAuth : MainEvent
    data object OnNavigateToPin : MainEvent

}
