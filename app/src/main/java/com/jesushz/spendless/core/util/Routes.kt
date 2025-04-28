package com.jesushz.spendless.core.util

import kotlinx.serialization.Serializable

sealed interface Routes {

    // Graphs
    @Serializable
    object AuthGraph : Routes

    // Screens And Graph
    @Serializable
    data object RegisterScreen : Routes

}
