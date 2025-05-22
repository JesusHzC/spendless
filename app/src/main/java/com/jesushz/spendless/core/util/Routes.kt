package com.jesushz.spendless.core.util

import com.jesushz.spendless.auth.domain.PinFlow
import com.jesushz.spendless.core.domain.preferences.PrefsFlow
import kotlinx.serialization.Serializable

sealed interface Routes {

    // Graphs
    @Serializable
    data object AuthGraph : Routes
    @Serializable
    data object DashboardGraph : Routes
    @Serializable
    data object SettingsGraph: Routes

    // Screens Auth Graph
    @Serializable
    data object RegisterScreen : Routes
    @Serializable
    data class PinScreen(val flow: PinFlow, val username: String? = null) : Routes
    @Serializable
    data object LoginScreen : Routes

    // Screens Dashboard Graph
    @Serializable
    data class PreferencesScreen(val flow: PrefsFlow) : Routes
    @Serializable
    data object DashboardScreen : Routes
    @Serializable
    data object AllTransactionsScreen : Routes

    // Screens Settings Graph
    @Serializable
    data object SettingsScreen: Routes

}
