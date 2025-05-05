package com.jesushz.spendless.core.util

import com.jesushz.spendless.auth.domain.PinFlow
import kotlinx.serialization.Serializable

sealed interface Routes {

    // Graphs
    @Serializable
    data object AuthGraph : Routes
    @Serializable
    data object DashboardGraph : Routes

    // Screens And Graph
    @Serializable
    data object RegisterScreen : Routes
    @Serializable
    data class PinScreen(val flow: PinFlow, val username: String? = null) : Routes
    @Serializable
    data object LoginScreen : Routes

    @Serializable
    data object PreferencesScreen : Routes

}
