package com.jesushz.spendless.core.util

import com.jesushz.spendless.auth.domain.PinFlow
import kotlinx.serialization.Serializable

sealed interface Routes {

    // Graphs
    @Serializable
    object AuthGraph : Routes

    // Screens And Graph
    @Serializable
    data object RegisterScreen : Routes
    @Serializable
    data class PinScreen(val flow: PinFlow) : Routes
    @Serializable
    data object LoginScreen : Routes

}
