package com.jesushz.spendless.core.util

import com.jesushz.spendless.auth.domain.PinFlow
import kotlinx.serialization.Serializable

sealed interface Routes {

    // Graphs
    @Serializable
    data object AuthGraph : Routes
    @Serializable
    data object DashboardGraph : Routes

    // Screens Auth Graph
    @Serializable
    data object RegisterScreen : Routes
    @Serializable
    data class PinScreen(val flow: PinFlow, val username: String? = null) : Routes
    @Serializable
    data object LoginScreen : Routes

    // Screens Dashboard Graph
    @Serializable
    data object PreferencesScreen : Routes
    @Serializable
    data object HomeScreen : Routes
    @Serializable
    data object AllTransactionsScreen : Routes

}
